package eu.xenit.custodian.domain;

import eu.xenit.custodian.domain.metadata.CompositeProjectMetadataAnalyzer;
import eu.xenit.custodian.domain.changes.CompositeUpdateChannel;
import eu.xenit.custodian.domain.scm.CompositeSourceControlHandler;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import eu.xenit.custodian.ports.spi.channel.UpdateChannelFactory;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzerFactory;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandlerFactory;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.core.OrderComparator;
import org.springframework.core.io.support.SpringFactoriesLoader;

public class CustodianFactory {

    private final Set<SourceControlHandler> scm;
    private final Set<ProjectMetadataAnalyzer> metadata;
    private final Set<UpdateChannel> channels;

    private CustodianFactory(FactorySettings settings) {
        this.scm = Collections.unmodifiableSet(settings.scm);
        this.metadata = Collections.unmodifiableSet(settings.metadata);
        this.channels = Collections.unmodifiableSet(settings.updateChannels);
    }

    /**
     * Create a {@link CustodianImpl} instance - the main component - configured with all the SPI Adapters
     *
     * @return a configured {@link CustodianImpl} instance
     */
    public Custodian create() {

        return new CustodianImpl(
                new CompositeSourceControlHandler(scm.stream().sorted(OrderComparator.INSTANCE)),
                new CompositeProjectMetadataAnalyzer(metadata),
                new CompositeUpdateChannel(channels)
        );
    }

    /**
     * Create a {@link CustodianFactory} with default settings.
     *
     * @return a {@link CustodianFactory} with default settings
     */
    public static CustodianFactory withDefaultSettings() {
        return create(settings -> {
            settings.withDefaultScmHandlers()
                    .withDefaultProjectMetedataAnalyzers()
                    .withDefaultUpdateChannels();
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
     * Get registered update channels
     *
     * @return the registered {@link UpdateChannel}s
     */
    Set<UpdateChannel> getUpdateChannels() {
        return channels;
    }

    /**
     * FactorySettings customizer for {@link CustodianFactory}.
     */
    public static final class FactorySettings {

        private final Set<SourceControlHandler> scm = new LinkedHashSet<>();
        private final Set<ProjectMetadataAnalyzer> metadata = new LinkedHashSet<>();
        private final Set<UpdateChannel> updateChannels = new LinkedHashSet<>();

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

        public FactorySettings withDefaultUpdateChannels() {
            this.clearUpdateChannels();
            SpringFactoriesLoader.loadFactories(UpdateChannelFactory.class,
                    UpdateChannelFactory.class.getClassLoader())
                    .stream()
                    .map(UpdateChannelFactory::create)
                    .forEach(this::withUpdateChannel);

            return this;
        }

        public FactorySettings withUpdateChannel(UpdateChannel updateChannel) {
            Objects.requireNonNull(updateChannel, "updateChannel can not be null");
            this.updateChannels.add(updateChannel);
            return this;
        }

        public FactorySettings clearUpdateChannels() {
            this.updateChannels.clear();
            return this;
        }

    }
}
