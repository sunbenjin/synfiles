package com.capinfo.synfiles.controller;

import com.capinfo.synfiles.SynfilesApplication;
import com.capinfo.synfiles.ftpserver.FtpClientUtils;
import com.capinfo.synfiles.utils.FileTestUtils;
import org.assertj.core.internal.Maps;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SynfilesApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        //       mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
    }
    /**
     *
     * Method: startftpServer(HttpServletRequest request, HttpServletResponse response)
     *
     */
    @Test
    public void test_01_StartftpServer() throws Exception {



        mvc.perform(MockMvcRequestBuilders.get("/ftpServer/start")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("ftpAddress", "C:\\litaolin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    /**
     *
     * Method: openDirMonitor(HttpServletRequest request, HttpServletResponse response)
     *
     */
    @Test
    public void test_03_OpenDirMonitor() throws Exception {
        String temp = "D:\\csImg;D:\\csImg2";
        
        mvc.perform(MockMvcRequestBuilders.get("/ftpServer/openMonitor")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("rootDirs", temp)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        String[] splitPath = temp.split(";");

        //检测监控是否打开
        File file = FileTestUtils.newRandomFile(splitPath[0]+"\\"+"111.txt");
        Assert.assertTrue(file.isFile());
        Thread.sleep(5000);
        //File fileServer = new File("C:\\litaolin\\111.txt");
        //Assert.assertTrue(fileServer.isFile());



        File file1 = FileTestUtils.newRandomFile(splitPath[1]+"\\"+"222.txt");
        Assert.assertTrue(file1.isFile());
        Thread.sleep(5000);
        //File fileServer1 = new File("C:\\litaolin\\222.txt");
        //Assert.assertTrue(fileServer1.isFile());


    }

    /**
     *
     * Method: stopDirMonitor(HttpServletRequest request, HttpServletResponse response)
     *
     */
    @Test
    public void test_04_StopDirMonitor() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/ftpServer/closeMonitor")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }




    /**
     *
     * Method: closeftpServer(HttpServletRequest request, HttpServletResponse response)
     *
     */
    @Test
    public void test_05_CloseftpServer() throws Exception {


        mvc.perform(MockMvcRequestBuilders.get("/ftpServer/close")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }

//    @Test
//    public void test_06_existFile() throws Exception {
//        File fileServer = new File("C:\\litaolin\\csImg\\111.txt");
//        Assert.assertTrue(fileServer.isFile());
//        File fileServer2 = new File("C:\\litaolin\\csImg2\\222.txt");
//        Assert.assertTrue(fileServer2.isFile());
//    }



}
