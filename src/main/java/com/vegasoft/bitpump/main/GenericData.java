package com.vegasoft.bitpump.main;

import java.util.ArrayList;
import java.util.List;

public class GenericData {
    private List<List<String>> data = new ArrayList<>(2000); // List of rows

    public void addColumn(String columnValue) {
        List<String> columnList;
        if(data.isEmpty()) {
            columnList = createNewRow();
        } else {
            columnList = data.get(data.size() - 1);
        }
        columnList.add(columnValue);
    }

    public void newRow() {
        createNewRow();
    }

    private List<String> createNewRow() {
        List<String> columnList;
        columnList = new ArrayList<>();
        data.add(columnList);
        return columnList;
    }

    public int getRowCount() {
        return data.size();
    }
}
