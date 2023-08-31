# LibSL specification skeleton generator for Java® standard library classes (JSL)

### Requirements

* JDK 11

### Usage example

```
jsl-spec-skeleton-generator class=java.util.ArrayList$ListItr renderer=primary generics=false include-inherited=true data-source=reflection
```

Expected output (std-err):

```
[i] CLI args:
- class=java.util.ArrayList$ListItr
- renderer=primary
- generics=false
- include-inherited=true
- data-source=reflection
```

Expected output (std-out):

```
libsl "1.1.0";

library std
    version "11"
    language "Java"
    url "https://github.com/openjdk/jdk11/blob/master/src/java.base/share/classes/java/util/ArrayList$ListItr.java";

// imports

import java.common;
import java/util/ArrayList;
import java/util/_interfaces;
import java/util/function/_interfaces;


// local semantic types

@extends("java.util.ArrayList$Itr")
@implements("java.util.ListIterator")
@public @private type ListItr
    is java.util.ArrayList$ListItr
    for Object
{
}


// automata

automaton ListItrAutomaton
(
)
: ListItr
{
    // states and shifts

    initstate Allocated;
    state Initialized;

    shift Allocated -> Initialized by [
        // constructors
        ListItr,
    ];

    shift Initialized -> self by [
        // instance methods
        add,
        equals,
        forEachRemaining,
        hasNext,
        hasPrevious,
        hashCode,
        next,
        nextIndex,
        previous,
        previousIndex,
        remove,
        set,
        toString,
    ];

    // internal variables

    // utilities

    // constructors

    @private constructor *.ListItr (@target self: ListItr, _this: ArrayList, index: int)
    {
        action TODO();
    }


    // static methods

    // methods

    fun *.add (@target self: ListItr, e: Object): void
    {
        action TODO();
    }


    // within java.lang.Object
    fun *.equals (@target self: ListItr, obj: Object): boolean
    {
        action TODO();
    }


    // within java.util.ArrayList.Itr
    fun *.forEachRemaining (@target self: ListItr, _action: Consumer): void
    {
        action TODO();
    }


    // within java.util.ArrayList.Itr
    fun *.hasNext (@target self: ListItr): boolean
    {
        action TODO();
    }


    fun *.hasPrevious (@target self: ListItr): boolean
    {
        action TODO();
    }


    // within java.lang.Object
    fun *.hashCode (@target self: ListItr): int
    {
        action TODO();
    }


    // within java.util.ArrayList.Itr
    fun *.next (@target self: ListItr): Object
    {
        action TODO();
    }


    fun *.nextIndex (@target self: ListItr): int
    {
        action TODO();
    }


    fun *.previous (@target self: ListItr): Object
    {
        action TODO();
    }


    fun *.previousIndex (@target self: ListItr): int
    {
        action TODO();
    }


    // within java.util.ArrayList.Itr
    fun *.remove (@target self: ListItr): void
    {
        action TODO();
    }


    fun *.set (@target self: ListItr, e: Object): void
    {
        action TODO();
    }


    // within java.lang.Object
    fun *.toString (@target self: ListItr): String
    {
        action TODO();
    }

}
```
