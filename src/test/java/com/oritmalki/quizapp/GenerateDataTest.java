package com.oritmalki.quizapp;

import com.oritmalki.quizapp.model.Answer;
import com.oritmalki.quizapp.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Orit on 31.1.2018.
 */

public class GenerateDataTest {

    public List<Question> questions = new ArrayList<>();

    String[] questionList = {
            "Who composed the piece \\“The Seasons\\”?",
            "Who of the following composers was a violinist?",
            "How many Preludes did Chopin wrote in his Op. 28?",
            "What is the nickname of Beethoven’s C# minor Sonata?",
            "What was Alex’s favorite piece of music in the movie “The Orange Clockwork”?",
            "Who of the following personals was also a musician?",
            "Which musical note will we hear in 440Hz frequency?",
            "Which performance direction will we write when we wish the player to gradually increase volume?",
            "Antoni Stradivari was a master-builder of:",
            "Which of the following instruments belongs to the percussion family?"
    };

    Answer[] a1 = {new Answer("Mozart", false), new Answer("Vivaldi", false),
            new Answer("Tchaikovsky", true), new Answer("Saint-Saens", false)};
    Answer[] a2 = {new Answer("Chopin", false), new Answer("Liszt", false),
            new Answer("Rachmaninov", false), new Answer("Paganini", true)};
    Answer[] a3 = {new Answer("10", false), new Answer("24", true),
            new Answer("12", false), new Answer("18", false)};
    Answer[] a4 = {new Answer("Moonlight", true), new Answer("Pastoral", false),
            new Answer("Waldstein", false), new Answer("Appassionata", false)};
    Answer[] a5 = {new Answer("Beethoven’s 5th Symphony", false), new Answer("Nino Rota’s Cinema Paradiso", false),
            new Answer("Beethoven’s 9th Symphony", true), new Answer("Richard Strauss’s Thus Spoke Zarathustra", false)};
    Answer[] a6 = {new Answer("Woody Allen", true), new Answer("Albert Einstein", true),
            new Answer("George Bush Sr", false), new Answer("Steven Spielberg", false),
            new Answer("Bill Clinton", true)};
    Answer[] a7 = {new Answer("Mozart", false), new Answer("Vivaldi", false),
            new Answer("Tchaikovsky", true), new Answer("Saint-Saens", false)};
    Answer[] a8 = {new Answer("crescendo", true)};
    Answer[] a9 = {new Answer("Organs", false), new Answer("Violins", true),
            new Answer("Pianos", false), new Answer("Guitars", false)};
    Answer[] a10 = {new Answer("Timbale", true), new Answer("Santur", false),
            new Answer("Piano", true), new Answer("Vibraphone", true),
            new Answer("Sousaphone", false), new Answer("Drums", true)};


    HashMap<String, Integer> AnswerSheet = new HashMap<>();




    public List<Question> getQuestionsData(List<Question> questions) {

        questions.add(new Question(questionList[1], getAnswerTypeOf(a1), a1));
        questions.add(new Question(questionList[2], getAnswerTypeOf(a2), a2));
        questions.add(new Question(questionList[3], getAnswerTypeOf(a3), a3));
        questions.add(new Question(questionList[4], getAnswerTypeOf(a4), a4));
        questions.add(new Question(questionList[5], getAnswerTypeOf(a5), a5));
        questions.add(new Question(questionList[6], getAnswerTypeOf(a6), a6));
        questions.add(new Question(questionList[7], getAnswerTypeOf(a7), a7));
        questions.add(new Question(questionList[8], getAnswerTypeOf(a8), a8));
        questions.add(new Question(questionList[9], getAnswerTypeOf(a9), a9));
        questions.add(new Question(questionList[10], getAnswerTypeOf(a10), a10));

        return questions;
    }

    public int getAnswerTypeOf(Answer[] answersArray) {
        int numOfRightAnswers = countRightAnswersInArray(answersArray);

        if (numOfRightAnswers == 1 && answersArray.length > 1) {
            return Answer.ONE_CORRECT_ANSWER;
        }
        else if (numOfRightAnswers > 1) {
            return Answer.MULTIPLE_ANSWERS;
        }
        else if (numOfRightAnswers == 1) {
            return Answer.TEXT_ANSWER;
        }
        return 0;
    }

    public int countRightAnswersInArray(Answer[] answersArray) {
        int count = 0;
        for (int i = 0; i<answersArray.length; i++) {

            if (answersArray[i].getCorrect() == true) {
                count++;
            }

        }
        return count;
    }



}
