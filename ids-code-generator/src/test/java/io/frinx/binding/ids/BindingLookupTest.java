/*
 * Copyright © 2021 Frinx and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.frinx.binding.ids;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.frinx.binding.ids.BindingLookup.GeneratedTypeMeta;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opendaylight.mdsal.binding.generator.impl.BindingGeneratorImpl;
import org.opendaylight.mdsal.binding.model.api.Type;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.parser.api.YangSyntaxErrorException;
import org.opendaylight.yangtools.yang.model.repo.api.YangTextSchemaSource;
import org.opendaylight.yangtools.yang.parser.rfc7950.reactor.RFC7950Reactors;
import org.opendaylight.yangtools.yang.parser.rfc7950.repo.YangStatementStreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BindingLookupTest {

    private static final Logger LOG = LoggerFactory.getLogger(BindingLookupTest.class);

    SchemaContext context;
    Set<Module> modules;
    List<Type> types;

    private static final List<String> EXP_SCHEMA_PATH = Lists.newArrayList(
            "AbsoluteSchemaPath{path=[(top?revision=2017-06-01)c1]}",
            "AbsoluteSchemaPath{path=[(top?revision=2017-06-01)c1, (top?revision=2017-06-01)c2]}",
            "AbsoluteSchemaPath{path=[(top?revision=2017-06-01)c1, (top?revision=2017-06-01)c2,"
                    + " (aug1?revision=2017-06-01)ca1]}");

    private static final List<String> EXP_TYPE_NAME = Lists.newArrayList(
            "org.opendaylight.yang.gen.v1.top.rev170601.g1.C1",
            "org.opendaylight.yang.gen.v1.top.rev170601.g2.C2",
            "org.opendaylight.yang.gen.v1.aug1.rev170601.c1.c2.Ca1");

    @BeforeEach
    void setUp() throws Exception {
        Path myPath = Paths.get("src/test/resources/yangs");
        List<YangStatementStreamSource> deviceYangFiles = Files.walk(myPath)
                .filter(path -> path.toFile().isFile() && path.toString().endsWith(".yang"))
                .map(path -> YangTextSchemaSource.forFile(path.toFile()))
                .map(textSchemaSource -> {
                    try {
                        return YangStatementStreamSource.create(textSchemaSource);
                    } catch (IOException | YangSyntaxErrorException e) {
                        LOG.info(e.toString());
                    }
                    return null;
                })
                .collect(Collectors.toList());

        context = RFC7950Reactors.defaultReactor()
                .newBuild()
                .addSources(deviceYangFiles)
                .buildEffective();
        modules = Sets.newHashSet(context.findModules("aug2"));
        modules.addAll(context.findModules("aug1"));
        modules.addAll(context.findModules("top"));
        types = new BindingGeneratorImpl().generateTypes(context);
    }


    @Test
    void iidsContainAugmentTest() throws IOException {
        IdsCodeGenerator ids = new IdsCodeGenerator();
        ids.setMavenProject(new MavenProject());
        Path tempDir = Files.createTempDirectory("iidsContainAugmentTest");
        Collection<File> files = ids.generateSources(context, tempDir.toFile(), modules, null);
        tempDir.toFile().deleteOnExit();
        Boolean iidsContainLongestAugmentation = Files.readAllLines(files.iterator().next().toPath())
                .stream()
                .anyMatch(line -> line.contains("C1_C2_CA_AUG_CA11_CA2"));
        assertEquals(true, iidsContainLongestAugmentation);
        deleteOutputFilesAndFolder(tempDir);
    }

    public void deleteOutputFilesAndFolder(Path tempDir) throws IOException {
        if (tempDir != null) {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void extractAugMetaTest() {
        for (Module module : modules) {
            Set<AugmentationSchemaNode> augNodes = module.getAugmentations();
            for (AugmentationSchemaNode augNode : augNodes) {
                List<GeneratedTypeMeta> currAugMetas =
                        BindingLookup.extractAugMeta(augNode, context, types);

                assertEquals(EXP_SCHEMA_PATH.get(0), currAugMetas.get(0).originalTargetPath.toString());
                assertEquals(EXP_TYPE_NAME.get(0), currAugMetas.get(0).targetType.getFullyQualifiedName());

                assertEquals(EXP_SCHEMA_PATH.get(1), currAugMetas.get(1).originalTargetPath.toString());
                assertEquals(EXP_TYPE_NAME.get(1), currAugMetas.get(1).targetType.getFullyQualifiedName());

                if (currAugMetas.size() == 3) {
                    assertEquals(EXP_SCHEMA_PATH.get(2), currAugMetas.get(2).originalTargetPath.toString());
                    assertEquals(EXP_TYPE_NAME.get(2), currAugMetas.get(2).targetType.getFullyQualifiedName());
                }
            }
        }
    }
}
