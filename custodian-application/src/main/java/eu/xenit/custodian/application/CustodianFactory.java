package eu.xenit.custodian.application;

import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPortFactory;
import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import eu.xenit.custodian.ports.spi.channel.UpdateChannelFactory;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPort;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzerFactory;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandlerFactory;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.Set;
import java.util.function.Consumer;


public class CustodianFactory {

    private final Set<BuildSystemPort> buildSystems;

    private final Set<SourceControlHandler> scm;

    private final Set<UpdateChannel> channels;

    private CustodianFactory(FactorySettings settings) {
        this.scm = Collections.unmodifiableSet(settings.scm);
        this.buildSystems = Collections.unmodifiableSet(settings.buildSystems);
        this.channels = Collections.unmodifiableSet(settings.updateChannels);
    }

    /**
     * Create a {@link CustodianImpl} instance - the main component - configured with all the SPI Adapters
     *
     * @return a configured {@link CustodianImpl} instance
     */
    public Custodian create() {
        return this.createApplication().getApi();
    }

    private CustodianApplication createApplication() {
        return new CustodianApplication(this.getBuildSystems(), this.getSourceControlHandlers());
    }

    /**
     * Create a {@link CustodianFactory} with default settings.
     *
     * @return a {@link CustodianFactory} with default settings
     */
    public static CustodianFactory withDefaultSettings() {
        return create(settings -> {
            settings.withDefaultScmHandlers()
                    .withDefaultBuildSystems()
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
     * Get registered {@link BuildSystemPort}
     *
     * @return the registered {@link BuildSystemPort}s
     */
    Set<BuildSystemPort> getBuildSystems() {
        return buildSystems;
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

        private final Set<BuildSystemPort> buildSystems = new LinkedHashSet<>();

        private final Set<SourceControlHandler> scm = new LinkedHashSet<>();
        private final Set<UpdateChannel> updateChannels = new LinkedHashSet<>();

        private FactorySettings() {

        }

        public FactorySettings withDefaultBuildSystems() {
            this.clearBuildSystems();
            ServiceLoader.load(BuildSystemPortFactory.class)
                    .stream()
                    .map(Provider::get)
                    .map(BuildSystemPortFactory::create)
                    .forEach(this::withBuildSystem);

            return this;
        }

        public FactorySettings withBuildSystem(BuildSystemPort buildSystem) {
            Objects.requireNonNull(buildSystem, "buildSystem can not be null");
            this.buildSystems.add(buildSystem);
            return this;
        }

        public FactorySettings clearBuildSystems() {
            this.buildSystems.clear();
            return this;
        }


        public FactorySettings withDefaultScmHandlers() {
            this.clearScmHandlers();
            ServiceLoader.load(SourceControlHandlerFactory.class)
                    .stream()
                    .map(Provider::get)
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



        public FactorySettings withDefaultUpdateChannels() {
            this.clearUpdateChannels();
            ServiceLoader.load(UpdateChannelFactory.class)
                    .stream()
                    .map(Provider::get)
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
