package ru.andreymarkelov.atlas.plugins.attrrem.manager;

public interface AttacherMgr {
    String[] getProjectKeys();

    void setProjectKeys(String[] projectKeys);

    boolean isActiveForAll();

    void setActiveForAll(boolean activeForAll);
}
