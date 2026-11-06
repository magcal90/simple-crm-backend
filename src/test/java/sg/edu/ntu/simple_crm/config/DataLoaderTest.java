package sg.edu.ntu.simple_crm.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import sg.edu.ntu.simple_crm.repository.CustomerRepository;

@SpringBootTest
@ActiveProfiles("test")
class DataLoaderTest {

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void repeatedStartupDoesNotDuplicateOrResetSampleCustomers() throws Exception {
        assertThat(customerRepository.count()).isEqualTo(4);

        dataLoader.run(new DefaultApplicationArguments());

        assertThat(customerRepository.count()).isEqualTo(4);
    }
}
