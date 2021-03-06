<#--

     Copyright 2012-2014 Gunnar Morling (http://www.gunnarmorling.de/)
     and/or other contributors as indicated by the @authors tag. See the
     copyright.txt file in the distribution for a full listing of all
     contributors.

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.

-->
<@compress single_line=true>
    <#-- method is either internal to the mapper class, or external (via uses) declaringMapper!=null -->
    <#if declaringMapper??>${mapperVariableName}.</#if>${name}<#if (parameters?size > 0)>( <@arguments/> )<#else>()</#if>
    <#macro arguments>
        <#list parameters as param>
            <#if param.targetType>
                <#-- a class is passed on for casting, see @TargetType -->
                <@includeModel object=ext.targetType/>.class
            <#else>
                <@includeModel object=assignment targetType=singleSourceParameterType raw=ext.raw/>
            </#if>
            <#if param_has_next>, </#if>
        </#list>
        <#-- context parameter, e.g. for builtin methods concerning date conversion -->
        <#if contextParam??>, ${contextParam}</#if>
    </#macro>
</@compress>
