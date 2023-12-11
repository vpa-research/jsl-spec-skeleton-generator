libsl "1.1.0";

library std
    version "11"
    language "Java"
    url "https://github.com/openjdk/jdk11/blob/master/src/java.base/share/classes/java/util/IntSummaryStatistics.java";

// imports

import java/lang/Object;
import java/lang/String;
import java/util/function/IntConsumer;


// local semantic types

@implements("java.util.function.IntConsumer")
@public type IntSummaryStatistics
    is java.util.IntSummaryStatistics
    for Object
{
}


// automata

automaton IntSummaryStatisticsAutomaton
(
)
: IntSummaryStatistics
{
    // states and shifts

    initstate Allocated;
    state Initialized;

    shift Allocated -> Initialized by [
        // constructors
        `<init>` (IntSummaryStatistics),
        `<init>` (IntSummaryStatistics, long, int, int, long),
    ];

    shift Initialized -> self by [
        // instance methods
        accept,
        combine,
        getAverage,
        getCount,
        getMax,
        getMin,
        getSum,
        toString,
    ];

    // internal variables

    // utilities

    // constructors

    fun *.`<init>` (@target self: IntSummaryStatistics)
    {
        action TODO();
    }


    @throws(["java.lang.IllegalArgumentException"])
    fun *.`<init>` (@target self: IntSummaryStatistics, count: long, min: int, max: int, sum: long)
    {
        action TODO();
    }


    // static methods

    // methods

    fun *.accept (@target self: IntSummaryStatistics, value: int): void
    {
        action TODO();
    }


    fun *.combine (@target self: IntSummaryStatistics, other: IntSummaryStatistics): void
    {
        action TODO();
    }


    @final fun *.getAverage (@target self: IntSummaryStatistics): double
    {
        action TODO();
    }


    @final fun *.getCount (@target self: IntSummaryStatistics): long
    {
        action TODO();
    }


    @final fun *.getMax (@target self: IntSummaryStatistics): int
    {
        action TODO();
    }


    @final fun *.getMin (@target self: IntSummaryStatistics): int
    {
        action TODO();
    }


    @final fun *.getSum (@target self: IntSummaryStatistics): long
    {
        action TODO();
    }


    fun *.toString (@target self: IntSummaryStatistics): String
    {
        action TODO();
    }

}
