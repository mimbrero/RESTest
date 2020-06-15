package es.us.isa.restest.generators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import es.us.isa.restest.configuration.TestConfigurationFilter;
import es.us.isa.restest.configuration.TestConfigurationIO;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.generators.AbstractTestCaseGenerator;
import es.us.isa.restest.generators.RandomTestCaseGenerator;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.testcases.writers.RESTAssuredWriter;

public class AmadeusRandomTestCaseGenerator {

	@Test
	public void amadeusSearchHotels() {
		

		// Load specification
		String OAISpecPath = "src/test/resources/Amadeus/spec.yaml";
		OpenAPISpecification spec = new OpenAPISpecification(OAISpecPath);
		
		
		// Load configuration
		TestConfigurationObject conf = TestConfigurationIO.loadConfiguration("src/test/resources/Amadeus/confTest.yaml", spec);
		
		// Set number of test cases to be generated on each path, on each operation
		int numTestCases = 10;
		
		// Create generator and filter
		AbstractTestCaseGenerator generator = new RandomTestCaseGenerator(spec, conf, numTestCases);
		
		List<TestConfigurationFilter> filters = new ArrayList<>();
		TestConfigurationFilter filter = new TestConfigurationFilter();
		filter.setPath("/hotels/search-airport");
		filter.addGetMethod();
		filters.add(filter);
		
		Collection<TestCase> testCases = generator.generate(filters);
		
		assertEquals("Incorrect number of test cases", numTestCases, testCases.size());
		
		// Write RESTAssured test cases
		String basePath = spec.getSpecification().getServers().get(0).getUrl();
		RESTAssuredWriter writer = new RESTAssuredWriter(OAISpecPath, "src/generation/java/restassured", "AmadeusHotelSearchTest", "restassured", basePath.toLowerCase());
		writer.setOAIValidation(true);
		writer.setLogging(true);
		writer.write(testCases);

	}

}
