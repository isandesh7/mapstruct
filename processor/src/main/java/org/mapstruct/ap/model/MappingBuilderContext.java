/**
 *  Copyright 2012-2014 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.ap.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import org.mapstruct.ap.model.assignment.Assignment;
import org.mapstruct.ap.model.common.Type;
import org.mapstruct.ap.model.common.TypeFactory;
import org.mapstruct.ap.model.source.Method;
import org.mapstruct.ap.model.source.SourceMethod;
import org.mapstruct.ap.option.Options;

/**
 * This class provides the context for the builders.
 * <p>
 * The context provides:
 * <ul>
 * <li>Input for the building process, such as the source model (mapping methods found) and mapper references.</li>
 * <li>Required factory, utility, reporting methods for building the mappings.</li>
 * <li>Means to harbor results produced by the builders, such as forged- and virtual mapping methods that should be
 * generated in a later stage.</li>
 * </ul>
 *
 * @author Sjaak Derksen
 */
public class MappingBuilderContext {

    /**
     * Resolves the most suitable way for mapping an element (property, iterable element etc.) from source to target.
     * There are 2 basic types of mappings:
     * <ul>
     * <li>conversions</li>
     * <li>methods</li>
     * </ul>
     * conversions are essentially one line mappings, such as String to Integer and Integer to Long methods come in some
     * varieties:
     * <ul>
     * <li>referenced mapping methods, these are methods implemented (or referenced) by the user. Sometimes indicated
     * with the 'uses' in the mapping annotations or part of the abstract mapper class</li>
     * <li>generated mapping methods (by means of MapStruct)</li>
     * <li>built in methods</li>
     * </ul>
     *
     * @author Sjaak Derksen
     */
    public interface MappingResolver {

        /**
         * returns a parameter assignment
         *
         * @param mappingMethod target mapping method
         * @param mappedElement used for error messages
         * @param sourceType parameter to match
         * @param targetType return type to match
         * @param targetPropertyName name of the target property
         * @param dateFormat used for formatting dates in build in methods that need context information
         * @param qualifiers used for further select the appropriate mapping method based on class and name
         * @param sourceReference call to source type as string
         *
         * @return an assignment to a method parameter, which can either be:
         * <ol>
         * <li>MethodReference</li>
         * <li>TypeConversion</li>
         * <li>Direct Assignment (empty TargetAssignment)</li>
         * <li>null, no assignment found</li>
         * </ol>
         */
        Assignment getTargetAssignment(Method mappingMethod, String mappedElement, Type sourceType, Type targetType,
                                       String targetPropertyName, String dateFormat, List<TypeMirror> qualifiers,
                                       String sourceReference);

        Set<VirtualMappingMethod> getUsedVirtualMappings();
    }

    private final TypeFactory typeFactory;
    private final Elements elementUtils;
    private final Types typeUtils;
    private final Messager messager;
    private final Options options;
    private final TypeElement mapperTypeElement;
    private final List<SourceMethod> sourceModel;
    private final List<MapperReference> mapperReferences;
    private final MappingResolver mappingResolver;
    private final List<MappingMethod> mappingsToGenerate = new ArrayList<MappingMethod>();

    public MappingBuilderContext(TypeFactory typeFactory,
                          Elements elementUtils,
                          Types typeUtils,
                          Messager messager,
                          Options options,
                          MappingResolver mappingResolver,
                          TypeElement mapper,
                          List<SourceMethod> sourceModel,
                          List<MapperReference> mapperReferences) {
        this.typeFactory = typeFactory;
        this.elementUtils = elementUtils;
        this.typeUtils = typeUtils;
        this.messager = messager;
        this.options = options;
        this.mappingResolver = mappingResolver;
        this.mapperTypeElement = mapper;
        this.sourceModel = sourceModel;
        this.mapperReferences = mapperReferences;
    }

    public TypeElement getMapperTypeElement() {
        return mapperTypeElement;
    }

    public List<SourceMethod> getSourceModel() {
        return sourceModel;
    }

    public List<MapperReference> getMapperReferences() {
        return mapperReferences;
    }

    public TypeFactory getTypeFactory() {
        return typeFactory;
    }

    public Elements getElementUtils() {
        return elementUtils;
    }

    public Types getTypeUtils() {
        return typeUtils;
    }

    public Messager getMessager() {
        return messager;
    }

    public Options getOptions() {
        return options;
    }

    public MappingResolver getMappingResolver() {
        return mappingResolver;
    }

    public List<MappingMethod> getMappingsToGenerate() {
        return mappingsToGenerate;
    }

    public Set<VirtualMappingMethod> getUsedVirtualMappings() {
        return mappingResolver.getUsedVirtualMappings();
    }
}
