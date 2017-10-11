package io.frinx.binding.ids;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.apache.maven.project.MavenProject;
import org.opendaylight.yangtools.yang.binding.BindingMapping;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang2sources.spi.BasicCodeGenerator;
import org.opendaylight.yangtools.yang2sources.spi.BuildContextAware;
import org.opendaylight.yangtools.yang2sources.spi.MavenProjectAware;
import org.sonatype.plexus.build.incremental.BuildContext;

public final class IdsCodeGenerator implements BasicCodeGenerator, BuildContextAware, MavenProjectAware {

    static final String INVALID_CHARS_MATCHER = "[^a-zA-Z]";
    private MavenProject project;

    @Override
    public Collection<File> generateSources(SchemaContext context, File outputBaseDir, Set<Module> currentModules) throws IOException {
        throw new UnsupportedOperationException("Deprecated method");
    }

    @Override
    public Collection<File> generateSources(SchemaContext context,
                                            File outputBaseDir,
                                            Set<Module> currentModules,
                                            Function<Module, Optional<String>> moduleResourcePathResolver) throws IOException {
        String packageNameSource = project.getGroupId() + "." + project.getName()
                .replaceAll(INVALID_CHARS_MATCHER, ".")
                .toLowerCase();
        packageNameSource = BindingMapping.normalizePackageName(packageNameSource);

        outputBaseDir = Arrays.stream(packageNameSource.split("\\."))
                .map(File::new)
                .reduce(outputBaseDir, (file, file2) -> new File(file, file2.getName()));

        try (VariableNameCache varCache = new VariableNameCache();
             GeneratorExecution execution = new GeneratorExecution(context, varCache, currentModules, packageNameSource)) {
            return execution.execute(outputBaseDir);
        }
    }

    @Override
    public void setAdditionalConfig(Map<String, String> additionalConfiguration) {
    }

    @Override
    public void setResourceBaseDir(File resourceBaseDir) {
    }

    @Override
    public void setBuildContext(BuildContext buildContext) {
    }

    @Override
    public void setMavenProject(MavenProject project) {
        this.project = project;
    }
}
