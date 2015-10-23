package ru.andreymarkelov.atlas.plugins.attrrem.workflow.function;

import java.util.Collection;
import java.util.Map;

import com.atlassian.jira.exception.RemoveException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.attachment.Attachment;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;

public class DeleteAttachmentsFunction extends AbstractJiraFunctionProvider {
    private final AttachmentManager attachmentManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final PermissionManager permissionManager;

    public DeleteAttachmentsFunction(
            AttachmentManager attachmentManager,
            JiraAuthenticationContext jiraAuthenticationContext,
            PermissionManager permissionManager) {
        this.attachmentManager = attachmentManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.permissionManager = permissionManager;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        MutableIssue issue = getIssue(transientVars);

        if (!permissionManager.hasPermission(ProjectPermissions.DELETE_ALL_ATTACHMENTS, issue, jiraAuthenticationContext.getLoggedInUser())) {
            throw new RuntimeException(getText("attachdelete.delete.error.permission"));
        }

        Collection<Attachment> atts = issue.getAttachments();
        for (Attachment att : atts) {
            try {
                attachmentManager.deleteAttachment(att);
            } catch (RemoveException e) {
                throw new RuntimeException(getText("attachdelete.delete.error.file", att.getFilename()));
            }
        }
    }

    private String getText(String key, String... args) {
        return jiraAuthenticationContext.getI18nHelper().getText(key, args);
    }
}
