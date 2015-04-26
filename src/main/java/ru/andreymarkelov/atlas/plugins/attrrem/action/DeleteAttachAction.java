package ru.andreymarkelov.atlas.plugins.attrrem.action;

import java.util.Collection;

import ru.andreymarkelov.atlas.plugins.attrrem.manager.AttacherMgr;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.exception.RemoveException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.attachment.Attachment;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.web.action.issue.AbstractViewIssue;

public class DeleteAttachAction extends AbstractViewIssue {
    private static final long serialVersionUID = 2431906489689708128L;

    private final AttacherMgr attacherMgr;
    private final JiraAuthenticationContext authenticationContext;
    private final PermissionManager permissionManager;

    public DeleteAttachAction(
            SubTaskManager subTaskManager,
            AttacherMgr attacherMgr,
            JiraAuthenticationContext authenticationContext,
            PermissionManager permissionManager) {
        super(subTaskManager);
        this.attacherMgr = attacherMgr;
        this.authenticationContext = authenticationContext;
        this.permissionManager = permissionManager;
    }

    @Override
    @com.atlassian.jira.security.xsrf.RequiresXsrfCheck
    protected String doExecute() throws Exception {
        MutableIssue issue = getIssueObject();
        if (!hasPermission(issue.getProjectObject())) {
            addErrorMessage(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.attrrem.action.error.permission"));
            return ERROR;
        }

        Collection<Attachment> atts = issue.getAttachments();
        AttachmentManager am = ComponentAccessor.getAttachmentManager();
        for (Attachment att : atts) {
            try {
                am.deleteAttachment(att);
            } catch (RemoveException e) {
                addErrorMessage(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.attrrem.action.error.file", att.getFilename()));
                return ERROR;
            }
        }
        return redirect("/browse/" + issue.getKey());
    }

    private boolean hasPermission(Project project) {
        if (project != null) {
            String[] projectKeys = attacherMgr.getProjectKeys();
            if (projectKeys != null) {
                for (String projectKey : projectKeys) {
                    if (projectKey.equals(project.getId().toString())) {
                        return permissionManager.hasPermission(ProjectPermissions.DELETE_ALL_ATTACHMENTS, getIssueObject(), getLoggedInApplicationUser());
                    }
                }
            }
        }
        return false;
    }

    private String redirect(String path) {
        if (isInlineDialogMode()) {
            return returnCompleteWithInlineRedirect(path);
        } else {
            return getRedirect(path);
        }
    }
}
