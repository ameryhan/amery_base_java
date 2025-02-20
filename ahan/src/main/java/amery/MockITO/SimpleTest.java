package amery.MockITO;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SimpleTest {

    @Test
    public void simpleTest() {
        //arrange   
        Iterator i = mock(Iterator.class);
        when(i.next()).thenReturn("Hello").thenReturn("World");
        //act   
        String result = i.next() + " " + i.next();
        //verify   
        verify(i, times(2)).next();
        //assert   
        assertEquals("Hello World", result);
    }
}  
