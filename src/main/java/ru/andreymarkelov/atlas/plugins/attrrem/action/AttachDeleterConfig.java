package ru.andreymarkelov.atlas.plugins.attrrem.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.xsrf.RequiresXsrfCheck;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.andreymarkelov.atlas.plugins.attrrem.manager.AttacherMgr;

public class AttachDeleterConfig extends JiraWebActionSupport {
    private static final long serialVersionUID = -8881690781888724545L;

    private final AttacherMgr attacherMgr;
    private final ProjectManager projectManager;
    private final GlobalPermissionManager globalPermissionManager;

    private boolean isSaved = false;
    private List<String> savedProjKeys;
    private String[] selectedProjKeys;
    private boolean forAllProjects;

    public AttachDeleterConfig(
            AttacherMgr attacherMgr,
            ProjectManager projectManager,
            GlobalPermissionManager globalPermissionManager) {
        this.attacherMgr = attacherMgr;
        this.projectManager = projectManager;
        this.globalPermissionManager = globalPermissionManager;
    }

    @Override
    public String doDefault() {
        if(!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }

        this.forAllProjects = attacherMgr.isActiveForAll();
        this.selectedProjKeys = attacherMgr.getProjectKeys();
        this.savedProjKeys = selectedProjKeys == null ? null : Arrays.asList(selectedProjKeys);

        return INPUT;
    }

    @Override
    @RequiresXsrfCheck
    protected String doExecute() {
        if(!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }

        attacherMgr.setActiveForAll(forAllProjects);
        attacherMgr.setProjectKeys(selectedProjKeys);
        if (selectedProjKeys != null) {
            savedProjKeys = Arrays.asList(attacherMgr.getProjectKeys());
        }
        setSaved(true);
        return getRedirect("AttachDeleterConfig!default.jspa?saved=true");
    }

    public Map<String, String> getAllProjects() {
        Map<String, String> allProjs = new TreeMap<String, String>();
        List<Project> projects = projectManager.getProjectObjects();
        if (projects != null) {
            for (Project project : projects) {
                allProjs.put(project.getId().toString(), getProjView(project.getKey(), project.getName()));
            }
        }
        return allProjs;
    }

    private String getProjView(String key, String name) {
        return (key + ": " + name);
    }

    public List<String> getSavedProjKeys() {
        return savedProjKeys;
    }

    public String[] getSelectedProjKeys() {
        return selectedProjKeys;
    }

    public boolean hasAdminPermission() {
        ApplicationUser user = getLoggedInUser();
        if (user == null) {
            return false;
        }
        return globalPermissionManager.hasPermission(GlobalPermissionKey.ADMINISTER, user);
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public void setSavedProjKeys(List<String> savedProjKeys) {
        this.savedProjKeys = savedProjKeys;
    }

    public void setSelectedProjKeys(String[] selectedProjKeys) {
        this.selectedProjKeys = selectedProjKeys;
    }

    public boolean isForAllProjects() {
        return forAllProjects;
    }

    public void setForAllProjects(boolean forAllProjects) {
        this.forAllProjects = forAllProjects;
    }
}
