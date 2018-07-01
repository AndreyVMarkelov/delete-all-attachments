package ru.andreymarkelov.atlas.plugins.attrrem.action;

import java.util.Collection;

import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.issue.attachment.AttachmentService;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.exception.RemoveException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.attachment.Attachment;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.web.action.issue.AbstractViewIssue;
import ru.andreymarkelov.atlas.plugins.attrrem.manager.AttacherMgr;

public class DeleteAttachAction extends AbstractViewIssue {
    private static final long serialVersionUID = 2431906489689708128L;

    private final AttacherMgr attacherMgr;
    private final JiraAuthenticationContext authenticationContext;
    private final PermissionManager permissionManager;
    private final AttachmentService attachmentService;
    private final AttachmentManager attachmentManager;

    public DeleteAttachAction(
            SubTaskManager subTaskManager,
            AttacherMgr attacherMgr,
            JiraAuthenticationContext authenticationContext,
            PermissionManager permissionManager,
            AttachmentService attachmentService,
            AttachmentManager attachmentManager) {
        super(subTaskManager);
        this.attacherMgr = attacherMgr;
        this.authenticationContext = authenticationContext;
        this.permissionManager = permissionManager;
        this.attachmentService = attachmentService;
        this.attachmentManager = attachmentManager;
    }

    @Override
    @RequiresXsrfCheck
    protected String doExecute() {
        MutableIssue issue = this.getMutableIssue();
        if (!hasPermission(issue.getProjectObject())) {
            addErrorMessage(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.attrrem.action.error.permission"));
            return ERROR;
        }

        JiraServiceContext jiraServiceContext = new JiraServiceContextImpl(authenticationContext.getLoggedInUser());
        Collection<Attachment> attachments = issue.getAttachments();
        for (Attachment attachment : attachments) {
            try {
                attachmentService.delete(jiraServiceContext, attachment.getId());
                attachmentManager.deleteAttachment(attachment);
            } catch (RemoveException e) {
                addErrorMessage(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.attrrem.action.error.file", attachment.getFilename()));
                return ERROR;
            }
        }
        return redirect();
    }

    private boolean hasPermission(Project project) {
        if (project != null) {
            String[] projectKeys = attacherMgr.getProjectKeys();
            if (projectKeys != null) {
                for (String projectKey : projectKeys) {
                    if (projectKey.equals(project.getId().toString())) {
                        return permissionManager.hasPermission(ProjectPermissions.DELETE_ALL_ATTACHMENTS, getIssueObject(), getLoggedInUser());
                    }
                }
            }
        }
        return false;
    }

    private String redirect() {
        return isInlineDialogMode() ? returnComplete() : getRedirect("/browse/" + getIssue().getString("key"));
    }
}
