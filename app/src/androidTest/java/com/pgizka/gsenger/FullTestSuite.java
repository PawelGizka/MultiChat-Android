package com.pgizka.gsenger;

import android.test.suitebuilder.TestSuiteBuilder;

import com.pgizka.gsenger.conversationView.sendMediaView.SendMediaPresenter;
import com.pgizka.gsenger.converstation.ConversationPresenterTest;
import com.pgizka.gsenger.converstation.SendMediaPresenterTest;
import com.pgizka.gsenger.gcm.NewMediaMessageCommandTest;
import com.pgizka.gsenger.gcm.NewTextMessageCommandTest;
import com.pgizka.gsenger.gcm.commands.NewMediaMessageCommand;
import com.pgizka.gsenger.gcm.commands.NewTextMessageCommand;
import com.pgizka.gsenger.provider.MessageRepository;
import com.pgizka.gsenger.provider.MessageRepositoryTest;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ConversationPresenterTest.class,
        SendMediaPresenterTest.class,
        NewTextMessageCommandTest.class,
        NewMediaMessageCommandTest.class,
        MessageRepositoryTest.class})
public class FullTestSuite {}
