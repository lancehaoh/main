package seedu.address.logic.parser;

import java.util.ArrayList;

import seedu.address.logic.commands.Command;

/**
 * Auto-correct user input command.
 */
public class AutoCorrectCommand {
    //By default, no message should be sent to user.
    private static String messageToUser = "";

    public void setMessageToUser(String messageToUser) {
        this.messageToUser = messageToUser;
    }

    public String getMessageToUser() {
        return messageToUser;
    }

    /**
     * Generate misspelt words with 1 alphabet mistake
     */
    public ArrayList<String> editDistance1(String word) {
        ArrayList<String> results = new ArrayList<String>();
        String formattedWord = word.toLowerCase();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        //Adding any one character (from the alphabet) anywhere in the word.
        for (int i = 0; i <= formattedWord.length(); i++) {
            for (int j = 0; j < alphabet.length(); j++) {
                String newWord = formattedWord.substring(0, i) + alphabet.charAt(j)
                        + formattedWord.substring(i, formattedWord.length());
                results.add(newWord);
            }
        }

        //Removing any one character from the words
        if (word.length() > 1) {
            for (int i = 0; i < formattedWord.length(); i++) {
                String newWord = formattedWord.substring(0, i) + formattedWord.substring(i + 1, formattedWord.length());
                results.add(newWord);
            }
        }

        //Transposing (switching) the order of any two adjacent characters in a word.
        if (word.length() > 1) {
            for (int i = 0; i < word.length() - 1; i++) {
                String newWord = formattedWord.substring(0, i) + formattedWord.charAt(i + 1) + formattedWord.charAt(i)
                        + formattedWord.substring(i + 2, formattedWord.length());
                results.add(newWord);
            }
        }

        //Substituting any character in the word with another character.
        for (int i = 0; i < word.length(); i++) {
            for (int j = 0; j < alphabet.length(); j++) {
                String newWord = formattedWord.substring(0, i) + alphabet.charAt(j)
                        + formattedWord.substring(i + 1, formattedWord.length());
                results.add(newWord);
            }
        }
        return results;
    }

    /**
     *  Given a word, attempts to correct the spelling of that word.
        - First, if the word is a known word, return the word.
        - Second, if the word has any known words edit-distance 1 away, return the one with
          the highest frequency, as recorded in NWORDS.
        - Third, if the word has any known words edit-distance 2 away, return the one with
          the highest frequency, as recorded in NWORDS. (HINT: what does applying
          "editDistance1" *again* to each word of its own output do?)
        - Finally, if no good replacements are found, return "Unknown Command".
    */
    public String correctWord (String misSpeltWord) {

        if (misSpeltWord == null || misSpeltWord.equals("")) {
            throw new IllegalArgumentException("Unknown Command");
        }

        //alias is not checked in auto-correct
        if (misSpeltWord.length() == 1) {
            return misSpeltWord;
        }

        String formattedMisSpeltword = misSpeltWord.toLowerCase();
        ArrayList<String> commandPool = new ArrayList<>(Command.getMapOfCommandFormats().keySet());
        final String defaultresult = "Unknown Command";
        String result = "";

        //No correction needs to be made
        if (commandPool.contains(formattedMisSpeltword)) {
            return formattedMisSpeltword;
        }

        //Either "unknown command" or the corrected command is returned
        for (String command : commandPool) {
            if (command.charAt(0) != formattedMisSpeltword.charAt(0)) {
                continue;
            }
            result = checkMisspeltWords(command, formattedMisSpeltword);
            if (!result.equals(defaultresult)) {
                return result;
            }
        }

        return result;
    }

    /**
     * Check input command with every possible misspelt word
     */
    public String checkMisspeltWords (String command, String input) {
        final String defaultResult = "Unknown Command";

        ArrayList<String> editDistance1Words = editDistance1(command);
        ArrayList<ArrayList<String>> editDistance2Words = new ArrayList<ArrayList<String>>();
        for (String editDistance1Word : editDistance1Words) {
            editDistance2Words.add(editDistance1(editDistance1Word));
        }

        if (editDistance1Words.contains(input)) {
            messageToUser = "Your command: " + input + ", is corrected to: " + command;
            return command;
        }

        for (ArrayList<String> editDistance2Word : editDistance2Words) {
            if (editDistance2Word.contains(input)) {
                messageToUser = "Your command: " + input + ", is corrected to: " + command;
                return command;
            }
        }

        return defaultResult;
    }
}