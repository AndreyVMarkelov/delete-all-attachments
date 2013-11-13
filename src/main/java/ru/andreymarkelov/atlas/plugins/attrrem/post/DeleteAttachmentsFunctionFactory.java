package ru.andreymarkelov.atlas.plugins.attrrem.post;

import java.util.Collections;
import java.util.Map;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;

public class DeleteAttachmentsFunctionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory {
    @Override
    public Map<String, ?> getDescriptorParams(Map<String, Object> descriptorParams) {
        return Collections.emptyMap();
    }

    @Override
    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
    }

    @Override
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
    }

    @Override
    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
    }
}
