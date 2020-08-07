package thkoeln.st.springtestlib.validation;

import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericValidationTests extends GenericTests {

    private Validator validator;

    public GenericValidationTests(WebApplicationContext appContext) {
        super(appContext);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void checkValidation(ObjectDescription objectDescription, int expectedViolations) throws Exception {
        Object object = objectBuilder.buildObject(objectDescription);

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

        System.out.println("These violations were found:");
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.toString());
        }

        assertEquals(expectedViolations, constraintViolations.size());
    }
}
