package amery.bean.copy;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrikaTest{

    static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Test
    public void givenSrcAndDest_whenMaps_thenCorrect() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Source.class, Dest.class);
        MapperFacade mapper = mapperFactory.getMapperFacade();
        Source src = new Source("Baeldung", 10);
        Dest dest = mapper.map(src, Dest.class);

        assertEquals(dest.getAge(), src.getAge());
        assertEquals(dest.getName(), src.getName());
    }

    @Test
    public void givenSrcAndDest_whenMapsUsingBoundMapper_thenCorrect() {
        BoundMapperFacade<Source, Dest> boundMapper = mapperFactory.getMapperFacade(Source.class, Dest.class);
        Source src = new Source("baeldung", 10);
        Dest dest = boundMapper.map(src);

        assertEquals(dest.getAge(), src.getAge());
        assertEquals(dest.getName(), src.getName());
    }
}
