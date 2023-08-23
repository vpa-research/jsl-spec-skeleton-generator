package org.libsl.skeletons.summary;

@FunctionalInterface
public interface ClassSummaryProducer {
    ClassSummary collectInfo();
}
