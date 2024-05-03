package com.example.storyapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineRule( val testDispatcher:TestDispatcher= UnconfinedTestDispatcher()): TestWatcher()
/*TestCoroutineScope by TestCoroutineScope(testDispatcher)*/ {
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        //cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}