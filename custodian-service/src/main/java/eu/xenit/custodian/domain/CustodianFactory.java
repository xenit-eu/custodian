package eu.xenit.custodian.domain;

import eu.xenit.custodian.domain.metadata.CompositeProjectMetadataAnalyzer;
import eu.xenit.custodian.domain.scm.CompositeSourceControlHandler;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzerFactory;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandlerFactory;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.gradle.internal.impldep.com.google.common.annotations.VisibleForTesting;
import org.springframework.core.OrderComparator;
import org.springframework.core.io.support.SpringFactoriesLoader;

public class CustodianFactory {

    private final Set<SourceControlHandler> scm;
    private final Set<ProjectMetadataAnalyzer> metadata;

    private CustodianFactory(FactorySettings settings) {
        this.scm = Collections.unmodifiableSet(settings.scm);
        this.metadata = Collections.unmodifiableSet(settings.metadata);
    }

    /**
     * Create a {@link CustodianImpl} instance - the main component - configured with all the SPI Adapters
     *
     * @return a configured {@link CustodianImpl} instance
     */
    public Custodian create() {
        return new CustodianImpl(
                composeSourceControlHandlers(scm),
                composeMetadataAnalyzers(metadata)
        );
    }

    CompositeProjectMetadataAnalyzer composeMetadataAnalyzers(Set<ProjectMetadataAnalyzer> analyzers) {
        return new CompositeProjectMetadataAnalyzer(analyzers);
    }

    /**
     * Composes the registered {@link SourceControlHandler}s by iterating over the registered handlers,
     * taking {@link org.springframework.core.Ordered} annotation into account.
     *
     * @return a SourceControlHandler that composes the registered SourceControlHandler
     */
    CompositeSourceControlHandler composeSourceControlHandlers(Set<SourceControlHandler> handlers) {

        return new CompositeSourceControlHandler(handlers.stream().sorted(OrderComparator.INSTANCE));
    }

    /**
     * Create a {@link CustodianFactory} with default settings.
     *
     * @return a {@link CustodianFactory} with default settings
     */
    public static CustodianFactory withDefaultSettings() {
        return create(settings -> {
            settings.withDefaultScmHandlers()
                    .withDefaultProjectMetedataAnalyzers();
        });
    }

    /**
     * Create a {@link CustodianFactory}.
     *
     * @param customizer a consumer of the factory-settings to apply further customizations
     * @return a {@link CustodianFactory}
     */
    public static CustodianFactory create(Consumer<FactorySettings> customizer) {
        FactorySettings settings = new FactorySettings();
        customizer.accept(settings);

        return new CustodianFactory(settings);
    }

    /**
     * Get registered source control handlers
     *
     * @return the registered {@link SourceControlHandler}s
     */
    Set<SourceControlHandler> getSourceControlHandlers() {
        return scm;
    }

    /**
     * Get registered source control handlers
     *
     * @return the registered {@link SourceControlHandler}s
     */
    Set<ProjectMetadataAnalyzer> getMetadataAnalyzers() {
        return metadata;
    }

    /**
     * FactorySettings customizer for {@link CustodianFactory}.
     */
    public static final class FactorySettings {

        private final Set<SourceControlHandler> scm = new LinkedHashSet<>();
        private final Set<ProjectMetadataAnalyzer> metadata = new LinkedHashSet<>();

        private FactorySettings() {

        }

        public FactorySettings withDefaultScmHandlers() {
            this.clearScmHandlers();
            SpringFactoriesLoader.loadFactories(SourceControlHandlerFactory.class,
                    SourceControlHandlerFactory.class.getClassLoader())
                    .stream()
                    .map(SourceControlHandlerFactory::createSourceControlHandler)
                    .forEach(this::withScmHandler);

            return this;
        }

        public FactorySettings clearScmHandlers() {
            this.scm.clear();
            return this;
        }

        public FactorySettings withScmHandler(SourceControlHandler handler) {
            Objects.requireNonNull(handler, "handler can not be null");
            this.scm.add(handler);
            return this;
        }

        public FactorySettings withDefaultProjectMetedataAnalyzers() {
            this.clearMetadataAnalyzers();
            SpringFactoriesLoader.loadFactories(ProjectMetadataAnalyzerFactory.class,
                    ProjectMetadataAnalyzerFactory.class.getClassLoader())
                    .stream()
                    .map(ProjectMetadataAnalyzerFactory::create)
                    .forEach(this::withProjectMedataAnalyzer);

            return this;
        }

        public FactorySettings withProjectMedataAnalyzer(ProjectMetadataAnalyzer analyzer) {
            Objects.requireNonNull(analyzer, "analyzer can not be null");
            this.metadata.add(analyzer);
            return this;
        }

        public FactorySettings clearMetadataAnalyzers() {
            this.metadata.clear();
            return this;
        }

    }
}
