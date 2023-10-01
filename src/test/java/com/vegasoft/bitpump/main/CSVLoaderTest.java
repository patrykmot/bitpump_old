package com.vegasoft.bitpump.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CSVLoaderTest {

    @Test
    public void testIt() {
        CSVDoor loader = new CSVDoor();
        GenericData gd = loader.readCSV(Utils.FILE_BITCOIN_CSV);

        Assertions.assertNotNull(gd);
        Assertions.assertEquals(45621, gd.getRowCount());
    }
}