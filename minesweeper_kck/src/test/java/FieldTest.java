import org.example.Field;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    @Test
    void testDefaultField() {
        Field field = new Field();
        assertEquals(0, field.getValue(), "Default field value should be 0");
        assertTrue(field.isHidden(), "Field should be hidden by default");
        assertFalse(field.isFlagged(), "Field should not be flagged by default");
    }

    @Test
    void testSetBomb() {
        Field field = new Field();
        field.setBomb();
        assertEquals(9, field.getValue(), "Field value should be 9 after setting a bomb");
    }

    @Test
    void testIncrease() {
        Field field = new Field();
        field.increse();
        assertEquals(1, field.getValue(), "Field value should increase by 1");

        field.increse();
        assertEquals(2, field.getValue(), "Field value should increase to 2");

        field.setBomb();
        field.increse();
        assertEquals(9, field.getValue(), "Field value should not increase if it is a bomb");
    }

    @Test
    void testUnfold() {
        Field field = new Field();
        field.unfold();
        assertFalse(field.isHidden(), "Field should be visible after unfolding");
    }

    @Test
    void testFlagging() {
        Field field = new Field();
        field.flag();
        assertTrue(field.isFlagged(), "Field should be flagged after calling flag()");

        field.unflag();
        assertFalse(field.isFlagged(), "Field should not be flagged after calling unflag()");
    }
}

