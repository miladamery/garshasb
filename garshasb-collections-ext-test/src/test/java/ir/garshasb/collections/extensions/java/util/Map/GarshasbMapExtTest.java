package ir.garshasb.collections.extensions.java.util.Map;

import manifold.test.api.ExtensionManifoldTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static manifold.rt.api.util.Pair.and;
public class GarshasbMapExtTest extends ExtensionManifoldTest {
    @Override
    public void testCoverage() {
        //testCoverage(GarshasbMapExt.class);
    }

    public void testIndexedAssignment()
    {
        Map<String, Foo<String>> map = new HashMap<>();
        map["a"] = new Foo<>( "A" );
        map["b"] = new Foo<>( "B" );
        assertEquals( 2, map.size() );
        assertEquals( new Foo<>( "A" ), map["a"] );
        assertEquals( new Foo<>( "B" ), map["b"] );
    }

    public void testMapOf()
    {
        Map<String, Integer> map = Map.mapOf( "Moe" and 100, "Larry" and 107, "Curly" and 111 );
        assertEquals( 3, map.size() );
        assertEquals( (Integer)100, map.get( "Moe") );
        assertEquals( (Integer)107, map.get( "Larry") );
        assertEquals( (Integer)111, map.get( "Curly") );
        try
        {
            // test map is unmodifiable
            map.put( "x", 0 );
            fail();
        }
        catch( Exception ignore )
        {
        }
    }

    static class Foo<T>
    {
        T _value;

        Foo( T value )
        {
            _value = value;
        }

        @Override
        public boolean equals( Object o )
        {
            if( this == o ) return true;
            if( o == null || getClass() != o.getClass() ) return false;
            Foo<?> foo = (Foo<?>)o;
            return Objects.equals( _value, foo._value );
        }

        @Override
        public int hashCode()
        {
            return Objects.hash( _value );
        }
    }
}
