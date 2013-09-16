package ru.andreymarkelov.atlas.plugins.attrrem;

import java.util.Collection;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.exception.RemoveException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.attachment.Attachment;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.web.action.issue.AbstractViewIssue;
import com.atlassian.jira.webtests.Permissions;

public class DeleteAttachAction extends AbstractViewIssue {
    private static final long serialVersionUID = 2431906489689708128L;

    private final AttacherMgr attacherMgr;

    public DeleteAttachAction(SubTaskManager subTaskManager, AttacherMgr attacherMgr) {
        super(subTaskManager);
        this.attacherMgr = attacherMgr;
    }

    @Override
    @com.atlassian.jira.security.xsrf.RequiresXsrfCheck
    protected String doExecute() throws Exception {
        MutableIssue issue = getIssueObject();
        if (!hasPermission(issue.getProjectObject())) {
            addErrorMessage(getText("attachdelete.delete.error.permission"));
            return ERROR;
        }

        Collection<Attachment> atts = issue.getAttachments();
        AttachmentManager am = ComponentAccessor.getAttachmentManager();
        for (Attachment att : atts) {
            try {
                am.deleteAttachment(att);
            } catch (RemoveException e) {
                addErrorMessage(getText("attachdelete.delete.error.file", att.getFilename()));
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
                        return getPermissionManager().hasPermission(Permissions.ATTACHMENT_DELETE_ALL, project, getLoggedInUser());
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
