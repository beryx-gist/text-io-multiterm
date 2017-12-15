/*
 * Copyright 2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.beryx.textio.multiterm;

import org.beryx.textio.TextIO;
import org.beryx.textio.swing.SwingTextTerminal;

import java.awt.*;

public class MultiTerm {
	public static void main(String[] args) {
		SwingTextTerminal mainTerm = new SwingTextTerminal();
		mainTerm.init();
		TextIO mainTextIO = new TextIO(mainTerm);

        mainTerm.println("######################");
        mainTerm.println("### Mega Simulator ###");
        mainTerm.println("######################\n\n");

        while(true) {
            mainTerm.setBookmark("mainLoop");
            String user = mainTextIO.newStringInputReader().read("Username");
            String device = mainTextIO.newStringInputReader()
                    .withNumberedPossibleValues("BTS-514 (MG)", "CryoMax Beta", "BScan T-131")
                    .read("Choose the simulated device");


            SwingTextTerminal childTerm = new SwingTextTerminal();
            childTerm.init();
            TextIO childTextIO = new TextIO(childTerm);
            // Place the child terminal next to the main terminal:
            Rectangle mainBounds = mainTerm.getFrame().getBounds();
            childTerm.getFrame().setLocation(mainBounds.x + mainBounds.width + 10, mainBounds.y);
            childTerm.getFrame().setPreferredSize(new Dimension(720, mainBounds.height));

            // Don't allow users to close the child terminal:
            childTerm.registerUserInterruptHandler(term -> {}, false);

            childTerm.println(device + " successfully initialized.\n");
            childTerm.println("Hello, " + user + "!\n");
            int age = childTextIO.newIntInputReader().read("How old are you?");
            childTerm.println("If you exercise more, you will be very healthy on your " + (age + 1) + " birthday.");
            childTextIO.newStringInputReader().withMinLength(0).read("\nPress enter to shut down the " + device + "... ");

            childTerm.dispose();

            if(!mainTextIO.newBooleanInputReader().withDefaultValue(true).read("\nRun again?")) break;

            mainTerm.resetToBookmark("mainLoop");
        }
        mainTerm.dispose();
	}
}
