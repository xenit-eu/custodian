package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.adapters.metadata.gradle.notation.NotationParserBuilder;
import eu.xenit.custodian.adapters.metadata.gradle.notation.NotationParserBuilder.DependencyNotationConverter;
import eu.xenit.custodian.adapters.metadata.gradle.notation.NotationParserBuilder.NotationParser;
import eu.xenit.custodian.domain.metadata.buildsystem.DependencyIdentifier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ExternalModuleIdentifier extends DependencyIdentifier {

    /**
     * The group of the module.
     *
     * @return module group
     */
    @Nonnull
    String getGroup();

    /**
     * The name of the module.
     *
     * @return module name
     */
    @Nonnull
    String getName();

    /**
     * The version of the module
     *
     * @return module version
     */
    @Nullable
    String getVersion();

    /**
     * Optional classifier of the module
     *
     * @return module classifier
     */
    @Nullable
    String getClassifier();

    /**
     * Optional extension of the module
     */
    @Nullable
    String getType();

    @Nonnull
    @Override
    default String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getGroup()).append(":").append(this.getName());

        if (this.getVersion() != null) {
            sb.append(":").append(this.getVersion());

        }

        if (this.getClassifier() != null) {
            if (this.getVersion() == null) {
                sb.append(":");
            }
            sb.append(this.getClassifier());
        }

        if (this.getType() != null) {
            sb.append("@").append(this.getType());
        }

        return sb.toString();
    }



    static ExternalModuleIdentifier from(String notation) {

        NotationParser<ExternalModuleIdentifier> parser = new NotationParserBuilder()
                .build(new ModuleIdentifierConverter());

        return parser.apply(notation);
    }

    class ModuleIdentifierConverter implements DependencyNotationConverter<ExternalModuleIdentifier> {

        @Override
        public ExternalModuleIdentifier apply(String group, String name, String version, String classifier,
                String type) {
            return new DefaultExternalModuleIdentifier(group, name, version, classifier, type);
        }
    }

//    default boolean matches(String notation) {
//        new NotationParserBuilder().build(
//    }
//
//    default boolean matches(String group, String name, String version, String classifier, String extension) {
//
//    }


}
