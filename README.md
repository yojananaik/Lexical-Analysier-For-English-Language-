# Lexical-Analysier-For-English-Language-


## Overview

This Git repository contains the source code for a text analysis application. The application features a graphical user interface (GUI) designed for easy interaction, allowing users to analyze text effortlessly. Key functionalities include the ability to enter text, initiate the analysis process using the "Run" button, and reset input/output fields with the "Clear" button. The results of the analysis, including token counts and matching lists for various categories, are displayed in the output text box.

## Features

- Graphical user interface (GUI) for easy interaction
- "Run" and "Clear" buttons for starting the analysis process and resetting input/output fields
- Output text box displays results of analysis, including count and matching lists of tokens for each category
- Token categories: nouns, verbs, adjectives, adverbs, numerals, punctuation, operators, and special characters
- Useful analytical tool for researchers, developers, linguists, and anyone interested in text analysis
- User-centered design with thoughtful text input and output sections and monospaced font selection

## User Interaction

The application's GUI simplifies interaction. Users can input text, start analysis using the "Run" button, and clear fields with the "Clear" button, providing control and accessibility.

## Output Presentation

Results are presented in the output text box, displaying counts and matching lists of tokens for each category. The clarity of presentation facilitates quick understanding of linguistic element distribution.

## Token Categories

Token categories and counts are presented, covering nouns, verbs, adjectives, adverbs, numerals, punctuation, operators, and special characters. This comprehensive analysis enhances understanding of language elements.

## Analytical Utility

The application serves as a valuable analytical tool for researchers, developers, and linguists. Users can gain insights into language usage, make judgments based on text structure, and derive meaningful conclusions.

## User-Centered Design

Thoughtful design ensures user comfort and effectiveness. Monospaced font selection improves reading and alignment, contributing to a polished GUI appearance.

## Libraries Used

- [Stanford CoreNLP](https://stanfordnlp.github.io/CoreNLP/): Used for natural language processing tasks such as tokenization, sentence splitting, and part-of-speech tagging.
- [configparser](https://docs.python.org/3/library/configparser.html): Used for handling program configurations.
- [logging](https://docs.python.org/3/library/logging.html): Used for logging relevant data and suppressing INFO messages.

## Phases

The program consists of three phases:

1. **Phase 1:** Creating a pipeline using Stanford CoreNLP with specific annotators for tokenization, sentence splitting, and part-of-speech tagging.
2. **Phase 2:** Extracting tokens from the text input and classifying them into categories based on part-of-speech tags. Results are displayed in the output text box.
3. **Phase 3:** Displaying the count and list of punctuation tokens and operator tokens.

## Team Members

- [Yashashree Bedmutha](https://github.com/yashhashhrrreee)
- [Ayush Karnawat](https://github.com/akprettyboi)

##Feel free to contribute or report issues!
