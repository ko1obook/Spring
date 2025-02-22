package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.education.controllers.SalesPeriodController;
import ru.education.entity.SalesPeriod;
import service.mock.MockSalesPeriodService;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SalesPeriodController.class, MockSalesPeriodService.class})
public class SalesPeriodControllerTest {

    @Autowired
    private SalesPeriodController salesPeriodController;

    private MockMvc mockMvc;

    private final static String URL = "http://localhost:8080/api/v1/sales_period";

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(salesPeriodController).build();
    }

    @Test
    public void findAllTest() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk());
    }

    @Test
    public void createTest() throws Exception {
        SalesPeriod salesPeriod = new SalesPeriod();
        salesPeriod.setId(5L);
        salesPeriod.setPrice(1000L);
        salesPeriod.setDateFrom(new Date());
        String requestJson = mapper.writeValueAsString(salesPeriod);
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateTest() throws Exception {
        SalesPeriod salesPeriod = new SalesPeriod();
        salesPeriod.setId(9999L);
        salesPeriod.setPrice(2000L);
        salesPeriod.setDateFrom(new Date());
        String requestJson = mapper.writeValueAsString(salesPeriod);
        mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete(URL + "/9999"))
                .andExpect(status().isNoContent());
    }
}
