package com.oritmalki.quizapp.Data;

import com.oritmalki.quizapp.model.Answer;
import com.oritmalki.quizapp.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Orit on 31.1.2018.
 */

public class GenerateData {

    public final List<Question> questions = new ArrayList<>();

    public final String[] questionList = new String[]{
            "Who composed the piece \'The Seasons\'?",
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

    public final Answer[] a1 = {new Answer("Mozart", false), new Answer("Vivaldi", false),
                new Answer("Tchaikovsky", true), new Answer("Saint-Saens", false)};
    public final Answer[] a2 = {new Answer("Chopin", false), new Answer("Liszt", false),
            new Answer("Rachmaninov", false), new Answer("Paganini", true)};
    public final Answer[] a3 = {new Answer("10", false), new Answer("24", true),
            new Answer("12", false), new Answer("18", false)};
    public final Answer[] a4 = {new Answer("Moonlight", true), new Answer("Pastoral", false),
            new Answer("Waldstein", false), new Answer("Appassionata", false)};
    public final Answer[] a5 = {new Answer("Beethoven’s 5th Symphony", false), new Answer("Nino Rota’s Cinema Paradiso", false),
            new Answer("Beethoven’s 9th Symphony", true), new Answer("Richard Strauss’s Thus Spoke Zarathustra", false)};
    public final Answer[] a6 = {new Answer("Woody Allen", true), new Answer("Albert Einstein", true),
            new Answer("George Bush Sr", false), new Answer("Steven Spielberg", false),
            new Answer("Bill Clinton", true)};
    public final Answer[] a7 = {new Answer("C", false), new Answer("G", false),
            new Answer("A", true), new Answer("F", false)};
    public final Answer[] a8 = {new Answer("crescendo", true)};
    public final Answer[] a9 = {new Answer("Organs", false), new Answer("Violins", true),
            new Answer("Pianos", false), new Answer("Guitars", false)};
    public final Answer[] a10 = {new Answer("Timbale", true), new Answer("Santur", false),
            new Answer("Piano", true), new Answer("Vibraphone", true),
            new Answer("Sousaphone", false), new Answer("Drums", true)};


    HashMap<String, Integer> AnswerSheet = new HashMap<>();




    public List<Question> getQuestionsData() {

        questions.add(new Question(0, questionList[0], getAnswerTypeOfArray(a1), a1));
        questions.add(new Question(1, questionList[1], getAnswerTypeOfArray(a2), a2));
        questions.add(new Question(2, questionList[2], getAnswerTypeOfArray(a3), a3));
        questions.add(new Question(3, questionList[3], getAnswerTypeOfArray(a4), a4));
        questions.add(new Question(4, questionList[4], getAnswerTypeOfArray(a5), a5));
        questions.add(new Question(5, questionList[5], getAnswerTypeOfArray(a6), a6));
        questions.add(new Question(6, questionList[6], getAnswerTypeOfArray(a7), a7));
        questions.add(new Question(7, questionList[7], getAnswerTypeOfArray(a8), a8));
        questions.add(new Question(8, questionList[8], getAnswerTypeOfArray(a9), a9));
        questions.add(new Question(9, questionList[9], getAnswerTypeOfArray(a10), a10));

        for (Question question : questions) {
            question.setType(getAnswerTypeOfArray(question.getAnswers()));
        }

        return questions;
    }

    public static int getAnswerTypeOfArray(Answer[] answersArray) {
        int numOfRightAnswers = countRightAnswersInArray(answersArray);
        int type=0;

        if (numOfRightAnswers == 1 && answersArray.length > 1)
            type = Answer.ONE_CORRECT_ANSWER;

        else if (numOfRightAnswers > 1)
            type =  Answer.MULTIPLE_ANSWERS;

        else if (numOfRightAnswers == 1)
            type = Answer.TEXT_ANSWER;

        return type;
    }

    public static int countRightAnswersInArray(Answer[] answersArray) {
        int count = 0;
        for (int i = 0; i<answersArray.length; i++) {

        if (answersArray[i].getCorrect() == true) {
            count++;
        }

        }
        return count;
    }


}
