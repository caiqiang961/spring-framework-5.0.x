/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.annotation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.core.NestedIOException;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * Parses a {@link Configuration} class definition, populating a collection of
 * {@link ConfigurationClass} objects (parsing a single Configuration class may result in
 * any number of ConfigurationClass objects because one Configuration class may import
 * another using the {@link Import} annotation).
 *
 * <p>This class helps separate the concern of parsing the structure of a Configuration
 * class from the concern of registering BeanDefinition objects based on the content of
 * that model (with the exception of {@code @ComponentScan} annotations which need to be
 * registered immediately).
 *
 * <p>This ASM-based implementation avoids reflection and eager class loading in order to
 * interoperate effectively with lazy class loading in a Spring ApplicationContext.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @author Phillip Webb
 * @author Sam Brannen
 * @author Stephane Nicoll
 * @since 3.0
 * @see ConfigurationClassBeanDefinitionReader
 */
class ConfigurationClassParser {

	private static final PropertySourceFactory DEFAULT_PROPERTY_SOURCE_FACTORY = new DefaultPropertySourceFactory();

	private static final Comparator<DeferredImportSelectorHolder> DEFERRED_IMPORT_COMPARATOR =
			(o1, o2) -> AnnotationAwareOrderComparator.INSTANCE.compare(o1.getImportSelector(), o2.getImportSelector());


	private final Log logger = LogFactory.getLog(getClass());

	private final MetadataReaderFactory metadataReaderFactory;

	private final ProblemReporter problemReporter;

	private final Environment environment;

	private final ResourceLoader resourceLoader;

	private final BeanDefinitionRegistry registry;

	private final ComponentScanAnnotationParser componentScanParser;

	private final ConditionEvaluator conditionEvaluator;

	private final Map<ConfigurationClass, ConfigurationClass> configurationClasses = new LinkedHashMap<>();

	private final Map<String, ConfigurationClass> knownSuperclasses = new HashMap<>();

	private final List<String> propertySourceNames = new ArrayList<>();

	private final ImportStack importStack = new ImportStack();

	@Nullable
	private List<DeferredImportSelectorHolder> deferredImportSelectors;


	/**
	 * Create a new {@link ConfigurationClassParser} instance that will be used
	 * to populate the set of configuration classes.
	 */
	public ConfigurationClassParser(MetadataReaderFactory metadataReaderFactory,
			ProblemReporter problemReporter, Environment environment, ResourceLoader resourceLoader,
			BeanNameGenerator componentScanBeanNameGenerator, BeanDefinitionRegistry registry) {

		this.metadataReaderFactory = metadataReaderFactory;
		this.problemReporter = problemReporter;
		this.environment = environment;
		this.resourceLoader = resourceLoader;
		this.registry = registry;
		this.componentScanParser = new ComponentScanAnnotationParser(
				environment, resourceLoader, componentScanBeanNameGenerator, registry);
		this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
	}


	public void parse(Set<BeanDefinitionHolder> configCandidates) {
		this.deferredImportSelectors = new LinkedList<>();

		for (BeanDefinitionHolder holder : configCandidates) {
			BeanDefinition bd = holder.getBeanDefinition();
			try {
				//三个重载逻辑一样
				//解析包名的配置类，解析其扫描到的所有的配置类，@Component和@Configuration的    @bean还没有到
				if (bd instanceof AnnotatedBeanDefinition) {
					parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
				}
				else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {
					parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
				}
				else {
					parse(bd.getBeanClassName(), holder.getBeanName());
				}
			}
			catch (BeanDefinitionStoreException ex) {
				throw ex;
			}
			catch (Throwable ex) {
				throw new BeanDefinitionStoreException(
						"Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
			}
		}
		//执行AutoConfigSelectors（在springboot中执行所有的自动配置类的加载到ConfigurationClass中，以便于在注册@Bean时，循环解析配置类
		//（本项目的配置类顺序在最前面(优先解析本项目的，这也是为什么我们使用@ConditionalOnBean依赖一个jar包中的bean时无效，先解析我们的，再解析外部的，导致解析我们的时候还没有外部的，自然导致@ConditionalOnBean注解始终不通过)））
		processDeferredImportSelectors();
	}

	protected final void parse(@Nullable String className, String beanName) throws IOException {
		Assert.notNull(className, "No bean class name for configuration class bean definition");
		MetadataReader reader = this.metadataReaderFactory.getMetadataReader(className);
		processConfigurationClass(new ConfigurationClass(reader, beanName));
	}

	protected final void parse(Class<?> clazz, String beanName) throws IOException {
		processConfigurationClass(new ConfigurationClass(clazz, beanName));
	}

	protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {
		processConfigurationClass(new ConfigurationClass(metadata, beanName));
	}

	/**
	 * Validate each {@link ConfigurationClass} object.
	 * @see ConfigurationClass#validate
	 */
	public void validate() {
		for (ConfigurationClass configClass : this.configurationClasses.keySet()) {
			configClass.validate(this.problemReporter);
		}
	}

	public Set<ConfigurationClass> getConfigurationClasses() {
		return this.configurationClasses.keySet();
	}


	protected void processConfigurationClass(ConfigurationClass configClass) throws IOException {
		//判断配置类是否存在@Conditional注解   并解析条件是否成立
		if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationPhase.PARSE_CONFIGURATION)) {
			return;
		}

		ConfigurationClass existingClass = this.configurationClasses.get(configClass);
		if (existingClass != null) {
			if (configClass.isImported()) {
				if (existingClass.isImported()) {
					existingClass.mergeImportedBy(configClass);
				}
				// Otherwise ignore new imported config class; existing non-imported class overrides it.
				return;
			}
			else {
				// Explicit bean definition found, probably replacing an import.
				// Let's remove the old one and go with the new one.
				this.configurationClasses.remove(configClass);
				this.knownSuperclasses.values().removeIf(configClass::equals);
			}
		}

		// Recursively process the configuration class and its superclass hierarchy.

		SourceClass sourceClass = asSourceClass(configClass);
		// 递归处理配置类和其所有父类  首先来到这里的是我们注测的最顶级的配置类，根据此配置类进行包扫描，然后找到其他的配置类
		do {
			sourceClass = doProcessConfigurationClass(configClass, sourceClass);
		}
		while (sourceClass != null);

		//把所有的配置类都加入到configurationClasses,这里一定是按照顺序来的，所以我们会出现不按照顺序无法使用@ConditionalOnBean依赖其他配置类中的@Bean的bean的问题
		this.configurationClasses.put(configClass, configClass);
	}

	/**
	 * Apply processing and build a complete {@link ConfigurationClass} by reading the
	 * annotations, members and methods from the source class. This method can be called
	 * multiple times as relevant sources are discovered.
	 * @param configClass the configuration class being build
	 * @param sourceClass a source class
	 * @return the superclass, or {@code null} if none found or previously processed
	 */
	@Nullable
	protected final SourceClass doProcessConfigurationClass(ConfigurationClass configClass, SourceClass sourceClass)
			throws IOException {

		// Recursively process any member (nested) classes first
		//进行当前配置类的父子级配置类的查找 并在其中进行递归解析
		processMemberClasses(configClass, sourceClass);

		// Process any @PropertySource annotations//处理@PropertySource注解
		for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), PropertySources.class,
				org.springframework.context.annotation.PropertySource.class)) {
			if (this.environment instanceof ConfigurableEnvironment) {
				processPropertySource(propertySource);
			}
			else {
				logger.warn("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
						"]. Reason: Environment must implement ConfigurableEnvironment");
			}
		}

		// Process any @ComponentScan annotations//处理@ComponentScan注解
		Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
		if (!componentScans.isEmpty() && !this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
			for (AnnotationAttributes componentScan : componentScans) {
				// The config class is annotated with @ComponentScan -> perform the scan immediately
				//处理@ComponentScan注解，扫描其中的路径，并将其中的@Configuration和@Component都注册为BeanDefinitionHolder并返回,
				//并进行第一次的condition判断  （我们的项目路径扫描源码解析）
				Set<BeanDefinitionHolder> scannedBeanDefinitions = this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
				// Check the set of scanned definitions for any further config classes and parse recursively if needed
				for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
					BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
					if (bdCand == null) {
						bdCand = holder.getBeanDefinition();
					}
					if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
						//进行递归
						parse(bdCand.getBeanClassName(), holder.getBeanName());
					}
				}
			}
		}

		// Process any @Import annotations
		//处理@import注解
		//ImportSelector   ImportBeanDefinitionRegistrar在导入这俩种类时，会直接注册其中对应方法的bean
		//导入其他则只当一个配置类bean而已
		//getImports(sourceClass)表示获取到类上的@Import注解的属性值class
		processImports(configClass, sourceClass, getImports(sourceClass), true);

		// Process any @ImportResource annotations//处理@ImportResource注解
		AnnotationAttributes importResource =
				AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
		if (importResource != null) {
			String[] resources = importResource.getStringArray("locations");
			Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
			for (String resource : resources) {
				String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
				configClass.addImportedResource(resolvedResource, readerClass);
			}
		}

		// Process individual @Bean methods
		// 处理配置类中的所有标注了@Bean注解的方法，并封装成BeanMethod添加到该配置类的实体ConfigurationClass中
		Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
		for (MethodMetadata methodMetadata : beanMethods) {
			configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
		}

		// Process default methods on interfaces
		processInterfaces(configClass, sourceClass);

		// Process superclass, if any
		if (sourceClass.getMetadata().hasSuperClass()) {
			String superclass = sourceClass.getMetadata().getSuperClassName();
			if (superclass != null && !superclass.startsWith("java") &&
					!this.knownSuperclasses.containsKey(superclass)) {
				this.knownSuperclasses.put(superclass, configClass);
				// Superclass found, return its annotation metadata and recurse
				return sourceClass.getSuperClass();
			}
		}

		// No superclass -> processing is complete
		return null;
	}

	/**
	 * Register member (nested) classes that happen to be configuration classes themselves.
	 */
	private void processMemberClasses(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
		Collection<SourceClass> memberClasses = sourceClass.getMemberClasses();
		if (!memberClasses.isEmpty()) {
			List<SourceClass> candidates = new ArrayList<>(memberClasses.size());
			//判断所有的父类是否有配置类，有则加入candidates
			for (SourceClass memberClass : memberClasses) {
				if (ConfigurationClassUtils.isConfigurationCandidate(memberClass.getMetadata()) &&
						!memberClass.getMetadata().getClassName().equals(configClass.getMetadata().getClassName())) {
					candidates.add(memberClass);
				}
			}
			OrderComparator.sort(candidates);
			for (SourceClass candidate : candidates) {
				if (this.importStack.contains(configClass)) {
					this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
				}
				else {
					this.importStack.push(configClass);
					try {
						//将找到的父类等进行递归解析
						processConfigurationClass(candidate.asConfigClass(configClass));
					}
					finally {
						this.importStack.pop();
					}
				}
			}
		}
	}

	/**
	 * Register default methods on interfaces implemented by the configuration class.
	 */
	private void processInterfaces(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
		for (SourceClass ifc : sourceClass.getInterfaces()) {
			Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(ifc);
			for (MethodMetadata methodMetadata : beanMethods) {
				if (!methodMetadata.isAbstract()) {
					// A default method or other concrete method on a Java 8+ interface...
					configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
				}
			}
			processInterfaces(configClass, ifc);
		}
	}

	/**
	 * Retrieve the metadata for all <code>@Bean</code> methods.
	 */
	private Set<MethodMetadata> retrieveBeanMethodMetadata(SourceClass sourceClass) {
		AnnotationMetadata original = sourceClass.getMetadata();
		Set<MethodMetadata> beanMethods = original.getAnnotatedMethods(Bean.class.getName());
		if (beanMethods.size() > 1 && original instanceof StandardAnnotationMetadata) {
			// Try reading the class file via ASM for deterministic declaration order...
			// Unfortunately, the JVM's standard reflection returns methods in arbitrary
			// order, even between different runs of the same application on the same JVM.
			try {
				AnnotationMetadata asm =
						this.metadataReaderFactory.getMetadataReader(original.getClassName()).getAnnotationMetadata();
				Set<MethodMetadata> asmMethods = asm.getAnnotatedMethods(Bean.class.getName());
				if (asmMethods.size() >= beanMethods.size()) {
					Set<MethodMetadata> selectedMethods = new LinkedHashSet<>(asmMethods.size());
					for (MethodMetadata asmMethod : asmMethods) {
						for (MethodMetadata beanMethod : beanMethods) {
							if (beanMethod.getMethodName().equals(asmMethod.getMethodName())) {
								selectedMethods.add(beanMethod);
								break;
							}
						}
					}
					if (selectedMethods.size() == beanMethods.size()) {
						// All reflection-detected methods found in ASM method set -> proceed
						beanMethods = selectedMethods;
					}
				}
			}
			catch (IOException ex) {
				logger.debug("Failed to read class file via ASM for determining @Bean method order", ex);
				// No worries, let's continue with the reflection metadata we started with...
			}
		}
		return beanMethods;
	}


	/**
	 * Process the given <code>@PropertySource</code> annotation metadata.
	 * @param propertySource metadata for the <code>@PropertySource</code> annotation found
	 * @throws IOException if loading a property source failed
	 */
	private void processPropertySource(AnnotationAttributes propertySource) throws IOException {
		String name = propertySource.getString("name");
		if (!StringUtils.hasLength(name)) {
			name = null;
		}
		String encoding = propertySource.getString("encoding");
		if (!StringUtils.hasLength(encoding)) {
			encoding = null;
		}
		String[] locations = propertySource.getStringArray("value");
		Assert.isTrue(locations.length > 0, "At least one @PropertySource(value) location is required");
		boolean ignoreResourceNotFound = propertySource.getBoolean("ignoreResourceNotFound");

		Class<? extends PropertySourceFactory> factoryClass = propertySource.getClass("factory");
		PropertySourceFactory factory = (factoryClass == PropertySourceFactory.class ?
				DEFAULT_PROPERTY_SOURCE_FACTORY : BeanUtils.instantiateClass(factoryClass));

		for (String location : locations) {
			try {
				String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
				Resource resource = this.resourceLoader.getResource(resolvedLocation);
				addPropertySource(factory.createPropertySource(name, new EncodedResource(resource, encoding)));
			}
			catch (IllegalArgumentException | FileNotFoundException | UnknownHostException ex) {
				// Placeholders not resolvable or resource not found when trying to open it
				if (ignoreResourceNotFound) {
					if (logger.isInfoEnabled()) {
						logger.info("Properties location [" + location + "] not resolvable: " + ex.getMessage());
					}
				}
				else {
					throw ex;
				}
			}
		}
	}

	private void addPropertySource(PropertySource<?> propertySource) {
		String name = propertySource.getName();
		MutablePropertySources propertySources = ((ConfigurableEnvironment) this.environment).getPropertySources();

		if (this.propertySourceNames.contains(name)) {
			// We've already added a version, we need to extend it
			PropertySource<?> existing = propertySources.get(name);
			if (existing != null) {
				PropertySource<?> newSource = (propertySource instanceof ResourcePropertySource ?
						((ResourcePropertySource) propertySource).withResourceName() : propertySource);
				if (existing instanceof CompositePropertySource) {
					((CompositePropertySource) existing).addFirstPropertySource(newSource);
				}
				else {
					if (existing instanceof ResourcePropertySource) {
						existing = ((ResourcePropertySource) existing).withResourceName();
					}
					CompositePropertySource composite = new CompositePropertySource(name);
					composite.addPropertySource(newSource);
					composite.addPropertySource(existing);
					propertySources.replace(name, composite);
				}
				return;
			}
		}

		if (this.propertySourceNames.isEmpty()) {
			propertySources.addLast(propertySource);
		}
		else {
			String firstProcessed = this.propertySourceNames.get(this.propertySourceNames.size() - 1);
			propertySources.addBefore(firstProcessed, propertySource);
		}
		this.propertySourceNames.add(name);
	}


	/**
	 * Returns {@code @Import} class, considering all meta-annotations.
	 */
	private Set<SourceClass> getImports(SourceClass sourceClass) throws IOException {
		Set<SourceClass> imports = new LinkedHashSet<>();
		Set<SourceClass> visited = new LinkedHashSet<>();
		collectImports(sourceClass, imports, visited);
		return imports;
	}

	/**
	 * Recursively collect all declared {@code @Import} values. Unlike most
	 * meta-annotations it is valid to have several {@code @Import}s declared with
	 * different values; the usual process of returning values from the first
	 * meta-annotation on a class is not sufficient.
	 * <p>For example, it is common for a {@code @Configuration} class to declare direct
	 * {@code @Import}s in addition to meta-imports originating from an {@code @Enable}
	 * annotation.
	 * @param sourceClass the class to search
	 * @param imports the imports collected so far
	 * @param visited used to track visited classes to prevent infinite recursion
	 * @throws IOException if there is any problem reading metadata from the named class
	 */
	private void collectImports(SourceClass sourceClass, Set<SourceClass> imports, Set<SourceClass> visited)
			throws IOException {

		if (visited.add(sourceClass)) {
			for (SourceClass annotation : sourceClass.getAnnotations()) {
				String annName = annotation.getMetadata().getClassName();
				if (!annName.startsWith("java") && !annName.equals(Import.class.getName())) {
					collectImports(annotation, imports, visited);
				}
			}
			imports.addAll(sourceClass.getAnnotationAttributes(Import.class.getName(), "value"));
		}
	}

	private void processDeferredImportSelectors() {
		//执行其deferredImportSelectors的子类和执行ImportSelector的子类的逻辑完全不同了，
		//实现了deferredImportSelector的子类会执行实现的方法getImportGroup(),在其中获取一个类，执行的是这个类的selectImport()

		List<DeferredImportSelectorHolder> deferredImports = this.deferredImportSelectors;
		this.deferredImportSelectors = null;
		if (deferredImports == null) {
			return;
		}

		deferredImports.sort(DEFERRED_IMPORT_COMPARATOR);
		Map<Object, DeferredImportSelectorGrouping> groupings = new LinkedHashMap<>();
		Map<AnnotationMetadata, ConfigurationClass> configurationClasses = new HashMap<>();
		for (DeferredImportSelectorHolder deferredImport : deferredImports) {
			Class<? extends Group> group = deferredImport.getImportSelector().getImportGroup();
			DeferredImportSelectorGrouping grouping = groupings.computeIfAbsent(
					(group != null ? group : deferredImport),
					key -> new DeferredImportSelectorGrouping(createGroup(group)));
			grouping.add(deferredImport);
			configurationClasses.put(deferredImport.getConfigurationClass().getMetadata(),
					deferredImport.getConfigurationClass());
		}
		for (DeferredImportSelectorGrouping grouping : groupings.values()) {
			//getImports()执行所有的selectImprot
			grouping.getImports().forEach(entry -> {
				ConfigurationClass configurationClass = configurationClasses.get(entry.getMetadata());
				try {
					processImports(configurationClass, asSourceClass(configurationClass),
							asSourceClasses(entry.getImportClassName()), false);
				}
				catch (BeanDefinitionStoreException ex) {
					throw ex;
				}
				catch (Throwable ex) {
					throw new BeanDefinitionStoreException(
							"Failed to process import candidates for configuration class [" +
							configurationClass.getMetadata().getClassName() + "]", ex);
				}
			});
		}
	}

	private Group createGroup(@Nullable Class<? extends Group> type) {
		Class<? extends Group> effectiveType = (type != null ? type : DefaultDeferredImportSelectorGroup.class);
		Group group = BeanUtils.instantiateClass(effectiveType);
		ParserStrategyUtils.invokeAwareMethods(group,
				ConfigurationClassParser.this.environment,
				ConfigurationClassParser.this.resourceLoader,
				ConfigurationClassParser.this.registry);
		return group;
	}

	private void processImports(ConfigurationClass configClass, SourceClass currentSourceClass,
			Collection<SourceClass> importCandidates, boolean checkForCircularImports) {

		if (importCandidates.isEmpty()) {
			return;
		}
			//检查是否重复执行  和   是否需要跳过
		if (checkForCircularImports && isChainedImportOnStack(configClass)) {
			this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
		}
		else {
			//记录  避免重复执行
			this.importStack.push(configClass);
			try {
				for (SourceClass candidate : importCandidates) {
					//@Import中属性值的类是否为ImportSelector子类，是则执行其selectImports()
					if (candidate.isAssignable(ImportSelector.class)) {
						// Candidate class is an ImportSelector -> delegate to it to determine imports
						Class<?> candidateClass = candidate.loadClass();
						ImportSelector selector = BeanUtils.instantiateClass(candidateClass, ImportSelector.class);//获取构造方法  反射创建对象
						ParserStrategyUtils.invokeAwareMethods(
								selector, this.environment, this.resourceLoader, this.registry);

						//如果是deferredImportSelectors的子类，则先不执行，待到最后，即parse方法执行完全结束后（所有的其他扫描全部结束后在执行）在最下面再执行
						if (this.deferredImportSelectors != null && selector instanceof DeferredImportSelector) {
							this.deferredImportSelectors.add(
									new DeferredImportSelectorHolder(configClass, (DeferredImportSelector) selector));
						}
						else {
							//执行其ImportSelector子类但不是deferredImportSelectors子类的selectImports()
							String[] importClassNames = selector.selectImports(currentSourceClass.getMetadata());
							Collection<SourceClass> importSourceClasses = asSourceClasses(importClassNames);
							//selectImports()返回的是类的全限定名称，又会扫描这些类是否有@Import注解，再循环执行
							processImports(configClass, currentSourceClass, importSourceClasses, false);
						}
					}
					//如果是ImportBeanDefinitionRegistrar方法的子类，先将其类反射出保存，之后会执行其registerBeanDefinitions()
					else if (candidate.isAssignable(ImportBeanDefinitionRegistrar.class)) {
						// Candidate class is an ImportBeanDefinitionRegistrar ->
						// delegate to it to register additional bean definitions
						Class<?> candidateClass = candidate.loadClass();
						ImportBeanDefinitionRegistrar registrar =
								BeanUtils.instantiateClass(candidateClass, ImportBeanDefinitionRegistrar.class);
						ParserStrategyUtils.invokeAwareMethods(
								registrar, this.environment, this.resourceLoader, this.registry);
						configClass.addImportBeanDefinitionRegistrar(registrar, currentSourceClass.getMetadata());
					}
					else {//如果不是上述俩种，那么就是一个类，加了@Configuration注解而已，正常配置类解析
						// Candidate class not an ImportSelector or ImportBeanDefinitionRegistrar ->
						// process it as an @Configuration class
						this.importStack.registerImport(
								currentSourceClass.getMetadata(), candidate.getMetadata().getClassName());
						processConfigurationClass(candidate.asConfigClass(configClass));
					}
				}
			}
			catch (BeanDefinitionStoreException ex) {
				throw ex;
			}
			catch (Throwable ex) {
				throw new BeanDefinitionStoreException(
						"Failed to process import candidates for configuration class [" +
						configClass.getMetadata().getClassName() + "]", ex);
			}
			finally {
				this.importStack.pop();
			}
		}
	}

	private boolean isChainedImportOnStack(ConfigurationClass configClass) {
		if (this.importStack.contains(configClass)) {
			String configClassName = configClass.getMetadata().getClassName();
			AnnotationMetadata importingClass = this.importStack.getImportingClassFor(configClassName);
			while (importingClass != null) {
				if (configClassName.equals(importingClass.getClassName())) {
					return true;
				}
				importingClass = this.importStack.getImportingClassFor(importingClass.getClassName());
			}
		}
		return false;
	}

	ImportRegistry getImportRegistry() {
		return this.importStack;
	}


	/**
	 * Factory method to obtain a {@link SourceClass} from a {@link ConfigurationClass}.
	 */
	private SourceClass asSourceClass(ConfigurationClass configurationClass) throws IOException {
		AnnotationMetadata metadata = configurationClass.getMetadata();
		if (metadata instanceof StandardAnnotationMetadata) {
			return asSourceClass(((StandardAnnotationMetadata) metadata).getIntrospectedClass());
		}
		return asSourceClass(metadata.getClassName());
	}

	/**
	 * Factory method to obtain a {@link SourceClass} from a {@link Class}.
	 */
	SourceClass asSourceClass(@Nullable Class<?> classType) throws IOException {
		if (classType == null) {
			return new SourceClass(Object.class);
		}
		try {
			// Sanity test that we can reflectively read annotations,
			// including Class attributes; if not -> fall back to ASM
			for (Annotation ann : classType.getAnnotations()) {
				AnnotationUtils.validateAnnotation(ann);
			}
			return new SourceClass(classType);
		}
		catch (Throwable ex) {
			// Enforce ASM via class name resolution
			return asSourceClass(classType.getName());
		}
	}

	/**
	 * Factory method to obtain {@link SourceClass SourceClasss} from class names.
	 */
	private Collection<SourceClass> asSourceClasses(String... classNames) throws IOException {
		List<SourceClass> annotatedClasses = new ArrayList<>(classNames.length);
		for (String className : classNames) {
			annotatedClasses.add(asSourceClass(className));
		}
		return annotatedClasses;
	}

	/**
	 * Factory method to obtain a {@link SourceClass} from a class name.
	 */
	SourceClass asSourceClass(@Nullable String className) throws IOException {
		if (className == null) {
			return new SourceClass(Object.class);
		}
		if (className.startsWith("java")) {
			// Never use ASM for core java types
			try {
				return new SourceClass(ClassUtils.forName(className, this.resourceLoader.getClassLoader()));
			}
			catch (ClassNotFoundException ex) {
				throw new NestedIOException("Failed to load class [" + className + "]", ex);
			}
		}
		return new SourceClass(this.metadataReaderFactory.getMetadataReader(className));
	}


	@SuppressWarnings("serial")
	private static class ImportStack extends ArrayDeque<ConfigurationClass> implements ImportRegistry {

		private final MultiValueMap<String, AnnotationMetadata> imports = new LinkedMultiValueMap<>();

		public void registerImport(AnnotationMetadata importingClass, String importedClass) {
			this.imports.add(importedClass, importingClass);
		}

		@Override
		@Nullable
		public AnnotationMetadata getImportingClassFor(String importedClass) {
			return CollectionUtils.lastElement(this.imports.get(importedClass));
		}

		@Override
		public void removeImportingClass(String importingClass) {
			for (List<AnnotationMetadata> list : this.imports.values()) {
				for (Iterator<AnnotationMetadata> iterator = list.iterator(); iterator.hasNext();) {
					if (iterator.next().getClassName().equals(importingClass)) {
						iterator.remove();
						break;
					}
				}
			}
		}

		/**
		 * Given a stack containing (in order)
		 * <ul>
		 * <li>com.acme.Foo</li>
		 * <li>com.acme.Bar</li>
		 * <li>com.acme.Baz</li>
		 * </ul>
		 * return "[Foo->Bar->Baz]".
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder("[");
			Iterator<ConfigurationClass> iterator = iterator();
			while (iterator.hasNext()) {
				builder.append(iterator.next().getSimpleName());
				if (iterator.hasNext()) {
					builder.append("->");
				}
			}
			return builder.append(']').toString();
		}
	}


	private static class DeferredImportSelectorHolder {

		private final ConfigurationClass configurationClass;

		private final DeferredImportSelector importSelector;

		public DeferredImportSelectorHolder(ConfigurationClass configClass, DeferredImportSelector selector) {
			this.configurationClass = configClass;
			this.importSelector = selector;
		}

		public ConfigurationClass getConfigurationClass() {
			return this.configurationClass;
		}

		public DeferredImportSelector getImportSelector() {
			return this.importSelector;
		}
	}


	private static class DeferredImportSelectorGrouping {

		private final DeferredImportSelector.Group group;

		private final List<DeferredImportSelectorHolder> deferredImports = new ArrayList<>();

		DeferredImportSelectorGrouping(Group group) {
			this.group = group;
		}

		public void add(DeferredImportSelectorHolder deferredImport) {
			this.deferredImports.add(deferredImport);
		}

		/**
		 * Return the imports defined by the group.
		 * @return each import with its associated configuration class
		 */
		public Iterable<Group.Entry> getImports() {
			for (DeferredImportSelectorHolder deferredImport : this.deferredImports) {
				this.group.process(deferredImport.getConfigurationClass().getMetadata(),
						deferredImport.getImportSelector());
			}
			return this.group.selectImports();
		}
	}


	private static class DefaultDeferredImportSelectorGroup implements Group {

		private final List<Entry> imports = new ArrayList<>();

		@Override
		public void process(AnnotationMetadata metadata, DeferredImportSelector selector) {
			for (String importClassName : selector.selectImports(metadata)) {
				this.imports.add(new Entry(metadata, importClassName));
			}
		}

		@Override
		public Iterable<Entry> selectImports() {
			return this.imports;
		}
	}


	/**
	 * Simple wrapper that allows annotated source classes to be dealt with
	 * in a uniform manner, regardless of how they are loaded.
	 */
	private class SourceClass implements Ordered {

		private final Object source;  // Class or MetadataReader

		private final AnnotationMetadata metadata;

		public SourceClass(Object source) {
			this.source = source;
			if (source instanceof Class) {
				this.metadata = new StandardAnnotationMetadata((Class<?>) source, true);
			}
			else {
				this.metadata = ((MetadataReader) source).getAnnotationMetadata();
			}
		}

		public final AnnotationMetadata getMetadata() {
			return this.metadata;
		}

		@Override
		public int getOrder() {
			Integer order = ConfigurationClassUtils.getOrder(this.metadata);
			return (order != null ? order : Ordered.LOWEST_PRECEDENCE);
		}

		public Class<?> loadClass() throws ClassNotFoundException {
			if (this.source instanceof Class) {
				return (Class<?>) this.source;
			}
			String className = ((MetadataReader) this.source).getClassMetadata().getClassName();
			return ClassUtils.forName(className, resourceLoader.getClassLoader());
		}

		public boolean isAssignable(Class<?> clazz) throws IOException {
			if (this.source instanceof Class) {
				return clazz.isAssignableFrom((Class<?>) this.source);
			}
			return new AssignableTypeFilter(clazz).match((MetadataReader) this.source, metadataReaderFactory);
		}

		public ConfigurationClass asConfigClass(ConfigurationClass importedBy) {
			if (this.source instanceof Class) {
				return new ConfigurationClass((Class<?>) this.source, importedBy);
			}
			return new ConfigurationClass((MetadataReader) this.source, importedBy);
		}

		public Collection<SourceClass> getMemberClasses() throws IOException {
			Object sourceToProcess = this.source;
			if (sourceToProcess instanceof Class) {
				Class<?> sourceClass = (Class<?>) sourceToProcess;
				try {
					Class<?>[] declaredClasses = sourceClass.getDeclaredClasses();
					List<SourceClass> members = new ArrayList<>(declaredClasses.length);
					for (Class<?> declaredClass : declaredClasses) {
						members.add(asSourceClass(declaredClass));
					}
					return members;
				}
				catch (NoClassDefFoundError err) {
					// getDeclaredClasses() failed because of non-resolvable dependencies
					// -> fall back to ASM below
					sourceToProcess = metadataReaderFactory.getMetadataReader(sourceClass.getName());
				}
			}

			// ASM-based resolution - safe for non-resolvable classes as well
			MetadataReader sourceReader = (MetadataReader) sourceToProcess;
			String[] memberClassNames = sourceReader.getClassMetadata().getMemberClassNames();
			List<SourceClass> members = new ArrayList<>(memberClassNames.length);
			for (String memberClassName : memberClassNames) {
				try {
					members.add(asSourceClass(memberClassName));
				}
				catch (IOException ex) {
					// Let's skip it if it's not resolvable - we're just looking for candidates
					if (logger.isDebugEnabled()) {
						logger.debug("Failed to resolve member class [" + memberClassName +
								"] - not considering it as a configuration class candidate");
					}
				}
			}
			return members;
		}

		public SourceClass getSuperClass() throws IOException {
			if (this.source instanceof Class) {
				return asSourceClass(((Class<?>) this.source).getSuperclass());
			}
			return asSourceClass(((MetadataReader) this.source).getClassMetadata().getSuperClassName());
		}

		public Set<SourceClass> getInterfaces() throws IOException {
			Set<SourceClass> result = new LinkedHashSet<>();
			if (this.source instanceof Class) {
				Class<?> sourceClass = (Class<?>) this.source;
				for (Class<?> ifcClass : sourceClass.getInterfaces()) {
					result.add(asSourceClass(ifcClass));
				}
			}
			else {
				for (String className : this.metadata.getInterfaceNames()) {
					result.add(asSourceClass(className));
				}
			}
			return result;
		}

		public Set<SourceClass> getAnnotations() {
			Set<SourceClass> result = new LinkedHashSet<>();
			for (String className : this.metadata.getAnnotationTypes()) {
				try {
					result.add(getRelated(className));
				}
				catch (Throwable ex) {
					// An annotation not present on the classpath is being ignored
					// by the JVM's class loading -> ignore here as well.
				}
			}
			return result;
		}

		public Collection<SourceClass> getAnnotationAttributes(String annType, String attribute) throws IOException {
			Map<String, Object> annotationAttributes = this.metadata.getAnnotationAttributes(annType, true);
			if (annotationAttributes == null || !annotationAttributes.containsKey(attribute)) {
				return Collections.emptySet();
			}
			String[] classNames = (String[]) annotationAttributes.get(attribute);
			Set<SourceClass> result = new LinkedHashSet<>();
			for (String className : classNames) {
				result.add(getRelated(className));
			}
			return result;
		}

		private SourceClass getRelated(String className) throws IOException {
			if (this.source instanceof Class) {
				try {
					Class<?> clazz = ClassUtils.forName(className, ((Class<?>) this.source).getClassLoader());
					return asSourceClass(clazz);
				}
				catch (ClassNotFoundException ex) {
					// Ignore -> fall back to ASM next, except for core java types.
					if (className.startsWith("java")) {
						throw new NestedIOException("Failed to load class [" + className + "]", ex);
					}
					return new SourceClass(metadataReaderFactory.getMetadataReader(className));
				}
			}
			return asSourceClass(className);
		}

		@Override
		public boolean equals(Object other) {
			return (this == other || (other instanceof SourceClass &&
					this.metadata.getClassName().equals(((SourceClass) other).metadata.getClassName())));
		}

		@Override
		public int hashCode() {
			return this.metadata.getClassName().hashCode();
		}

		@Override
		public String toString() {
			return this.metadata.getClassName();
		}
	}


	/**
	 * {@link Problem} registered upon detection of a circular {@link Import}.
	 */
	private static class CircularImportProblem extends Problem {

		public CircularImportProblem(ConfigurationClass attemptedImport, Deque<ConfigurationClass> importStack) {
			super(String.format("A circular @Import has been detected: " +
					"Illegal attempt by @Configuration class '%s' to import class '%s' as '%s' is " +
					"already present in the current import stack %s", importStack.element().getSimpleName(),
					attemptedImport.getSimpleName(), attemptedImport.getSimpleName(), importStack),
					new Location(importStack.element().getResource(), attemptedImport.getMetadata()));
		}
	}

}
