package com.liviridi.tools.wow.skills;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

import com.liviridi.tools.common.exception.UnableToInitializeException;
import com.liviridi.tools.common.util.ExcelUtil;

public class SkillArranger {

    private Sheet skillTable_;

    private static final CellAddress SPEC_1_START_ADDR = new CellAddress("A3");
    private Spec spec1_;

    private static final CellAddress SPEC_2_START_ADDR = new CellAddress("A8");
    private Spec spec2_;

    private static final CellAddress SPEC_3_START_ADDR = new CellAddress("A13");
    private Spec spec3_;

    private static final CellAddress OUTPUT_START_ADDR = new CellAddress("A26");

    private Map<CellAddress, String> arrangeResult_;

    public SkillArranger(Sheet sheet) throws UnableToInitializeException {
        if (sheet == null) {
            throw new UnableToInitializeException("sheet is null !\nunable to initialize");
        }
        skillTable_ = sheet;
        arrangeResult_ = new HashMap<CellAddress, String>();
        initSpecs();
    }

    private void initSpecs() {
        spec1_ = new Spec(ExcelUtil.getCellValue(skillTable_, SPEC_1_START_ADDR));
        spec1_.initBySheet(skillTable_, SPEC_1_START_ADDR);
        spec2_ = new Spec(ExcelUtil.getCellValue(skillTable_, SPEC_2_START_ADDR));
        spec2_.initBySheet(skillTable_, SPEC_2_START_ADDR);
        spec3_ = new Spec(ExcelUtil.getCellValue(skillTable_, SPEC_3_START_ADDR));
        spec3_.initBySheet(skillTable_, SPEC_3_START_ADDR);
    }

    public void doArrangement() {
        Set<String> spec1Skills = new LinkedHashSet<String>();
        spec1Skills.addAll(spec1_.getSkills());
        Set<String> spec2Skills = new LinkedHashSet<String>();
        spec2Skills.addAll(spec2_.getSkills());
        Set<String> spec3Skills = new LinkedHashSet<String>();
        spec3Skills.addAll(spec3_.getSkills());

        Set<String> spec1s = new LinkedHashSet<String>();
        spec1s.addAll(spec1Skills);
        Set<String> spec2s = new LinkedHashSet<String>();
        spec2s.addAll(spec2Skills);
        Set<String> spec3s = new LinkedHashSet<String>();
        spec3s.addAll(spec3Skills);

        int out1Row = OUTPUT_START_ADDR.getRow() + 1;
        int out2Row = OUTPUT_START_ADDR.getRow() + 2;
        int out3Row = OUTPUT_START_ADDR.getRow() + 3;

        int crtCol = OUTPUT_START_ADDR.getColumn();

        for (String skill : spec1s) {
            if (spec2Skills.contains(skill) || spec3Skills.contains(skill)) {
                if (spec2Skills.contains(skill)) {
                    arrangeResult_.put(new CellAddress(out2Row, crtCol), skill);
                    spec2Skills.remove(skill);
                }
                if (spec3Skills.contains(skill)) {
                    arrangeResult_.put(new CellAddress(out3Row, crtCol), skill);
                    spec3Skills.remove(skill);
                }
                arrangeResult_.put(new CellAddress(out1Row, crtCol), skill);
                spec1Skills.remove(skill);
                crtCol++;
            }
        }
        for (String skill : spec2s) {

            if (spec3Skills.contains(skill)) {
                arrangeResult_.put(new CellAddress(out3Row, crtCol), skill);
                spec3Skills.remove(skill);
                arrangeResult_.put(new CellAddress(out2Row, crtCol), skill);
                spec2Skills.remove(skill);
                crtCol++;
            }
        }
        int out1Col = crtCol;
        int out2Col = crtCol;
        int out3Col = crtCol;
        for (String skill : spec1Skills) {
            arrangeResult_.put(new CellAddress(out1Row, out1Col), skill);
            // spec1Skills.remove(skill);
            out1Col++;
        }
        for (String skill : spec2Skills) {
            arrangeResult_.put(new CellAddress(out2Row, out2Col), skill);
            // spec2Skills.remove(skill);
            out2Col++;
        }
        for (String skill : spec3Skills) {
            arrangeResult_.put(new CellAddress(out3Row, out3Col), skill);
            // spec3Skills.remove(skill);
            out3Col++;
        }
    }

    public void doOutput() {
        for (Entry<CellAddress, String> outEle : arrangeResult_.entrySet()) {
            int thisRow = outEle.getKey().getRow();
            int thisCol = outEle.getKey().getColumn();
            Row row = skillTable_.getRow(thisRow);
            if (row == null) {
                row = skillTable_.createRow(thisRow);
            }
            Cell cell = row.getCell(thisCol);
            if (cell == null) {
                cell = row.createCell(thisCol);
            }
            cell.setCellValue(outEle.getValue());
        }
    }

    public Spec getSpec1() {
        return spec1_;
    }

    public Spec getSpec2() {
        return spec2_;
    }

    public Spec getSpec3() {
        return spec3_;
    }
}
