package com.example.javaproto;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class GraphViewInteractionTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAddNodeOnSingleTap() {
        // Initially get number of nodes
        final int[] initialNodeCount = new int[1];
        activityScenarioRule.getScenario().onActivity(activity -> {
            Graph graph = activity.findViewById(R.id.customGraphView);
            initialNodeCount[0] = graph.getAdjacencyList().size();
        });
        // Simulate a single tap
        onView(withId(R.id.customGraphView)).perform(click());

        // Fetch the state again and verify addition of a node
        activityScenarioRule.getScenario().onActivity(activity -> {
            Graph graph = activity.findViewById(R.id.customGraphView);
            int updatedNodeCount = graph.getAdjacencyList().size();
            assertEquals("A node should have been added to the graph.", initialNodeCount[0] + 1, updatedNodeCount);
        });

    }



}
