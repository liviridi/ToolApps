package com.liviridi.tools.wow.skills;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

import com.liviridi.tools.common.util.ExcelUtil;

public class Spec {

    /** spec name */
    String name_;

    /** skill list */
    Set<String> skills_;

    public Spec(String name) {
        name_ = name;
        skills_ = new LinkedHashSet<String>();
    }

    public void initBySheet(Sheet sheet, CellAddress startAddr) {
        CellAddress realAddr = ExcelUtil.nextRowAddr(sheet, startAddr);
        String cellVal = null;
        do {
            cellVal = ExcelUtil.getCellValue(sheet, realAddr);
            if (cellVal == null)
                break;
            addSkill(cellVal);
            realAddr = ExcelUtil.nextColAddr(sheet, realAddr);
        } while (true);
    }

    /**
     * set skill list
     */
    public void setSkills(Set<String> skills) {
        skills_ = skills;
    }

    /**
     * add skill list
     */
    public void addSkills(Set<String> skills) {
        skills_.addAll(skills);
    }

    /**
     * add skill
     */
    public void addSkill(String skills) {
        skills_.add(skills);
    }

    /**
     * get skill list
     */
    public Set<String> getSkills() {
        return skills_;
    }
}
