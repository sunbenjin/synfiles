package com.capinfo.synfiles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

public class SynfilesApplicationTests {

    @Test
    public void contextLoads() {
        String temps = "C:\\litaolin2\\csImg2\\4356677(3) - 副本.txt";
        File file = new File(temps);
        System.out.println(file.getParent());

    }

}

