package com.example.quizapp.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.quizapp.R;
import com.example.quizapp.databinding.FragmentQuizBinding;
import com.example.quizapp.model.QuestionModel;
import com.example.quizapp.viewmodel.QuestionViewModel;

import java.util.HashMap;
import java.util.List;

public class QuizFragment extends Fragment {
    private QuestionViewModel viewModel;
    private NavController navController;
    private FragmentQuizBinding binding;
    private String quizId, answer;
    private CountDownTimer countDownTimer;
    private Long timer;
    private Long totalQueCount;
    private int notAnswer = 0;
    private int wrongAnswer = 0;
    private int correctAnswer = 0;
    private int currentQuestionNo = 0;
    private boolean canAnswer = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        navController = Navigation.findNavController(view);
        binding.imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_quizragment_to_listFragment);
            }
        });
        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        totalQueCount = QuizFragmentArgs.fromBundle(getArguments()).getTotalQueCount();
        viewModel.setQuizId(quizId);
        viewModel.getQuestion();
        LoadData();

        binding.option1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAnswer(binding.option1Btn);

            }
        });
        binding.option2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAnswer(binding.option2Btn);

            }
        });
        binding.option3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAnswer(binding.option3Btn);

            }
        });
        binding.nextQueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionNo == totalQueCount) {
                    submitResult();
                } else {
                    currentQuestionNo++;
                    loadQuestion(currentQuestionNo);
                    resetOption();
                }
            }
        });


    }

    private void LoadData() {
        enableOption();
        loadQuestion(1);
    }

    private void loadQuestion(int i) {
        currentQuestionNo = i;

        viewModel.getQuestionListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                QuestionModel question = questionModels.get(i - 1);
                binding.option1Btn.setText(question.getOption_a());
                binding.option2Btn.setText(question.getOption_b());
                binding.option3Btn.setText(question.getOption_c());
                binding.quizQuestionTv.setText(String.valueOf(currentQuestionNo) + ") " + question.getQuestion());
                binding.quizQuestionsCount.setText(String.valueOf(currentQuestionNo));
                timer = question.getTimer();
                answer = question.getAnswer();
                startTimer();
            }


        });
        canAnswer = true;
    }

    private void startTimer() {
        binding.countTimeQuiz.setText(timer.toString());
        binding.quizCoutProgressBar.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(timer * 1000, 1000) {
            @Override
            public void onTick(long l) {
                binding.countTimeQuiz.setText(String.valueOf(l / 1000));
                Long percent = l / (timer * 10);
                binding.quizCoutProgressBar.setProgress(percent.intValue());

            }

            @Override
            public void onFinish() {
                canAnswer = false;
                binding.ansFeedbackTv.setText("Times Up !! No answer selected");
                notAnswer++;
                showNextQuestion();
            }
        }.start();
    }


    private void enableOption() {
        //enable option
        binding.option1Btn.setVisibility(View.VISIBLE);
        binding.option2Btn.setVisibility(View.VISIBLE);
        binding.option3Btn.setVisibility(View.VISIBLE);
        //
        binding.option1Btn.setEnabled(true);
        binding.option2Btn.setEnabled(true);
        binding.option3Btn.setEnabled(true);

        binding.nextQueBtn.setVisibility(View.INVISIBLE);
        binding.ansFeedbackTv.setVisibility(View.INVISIBLE);

    }


    private void resetOption() {
        binding.ansFeedbackTv.setVisibility(View.INVISIBLE);
        binding.nextQueBtn.setVisibility(View.INVISIBLE);
        binding.nextQueBtn.setEnabled(false);
        binding.option1Btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.option_shape));
        binding.option2Btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.option_shape));
        binding.option3Btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.option_shape));
    }

    private void submitResult() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("correctAnswer", correctAnswer);
        result.put("noAnswer", notAnswer);
        result.put("wrongAnswer", wrongAnswer);
        viewModel.setResult(result);
        QuizFragmentDirections.ActionQuizragmentToResultFragment action =
                QuizFragmentDirections.actionQuizragmentToResultFragment();
        action.setQuizId(quizId);
        navController.navigate(action);


    }

    private void verifyAnswer(Button button) {
        if (canAnswer) {
            if (answer.equals(button.getText())) {
                button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_correct));
                binding.ansFeedbackTv.setText("Correct Answer");
                binding.ansFeedbackTv.setTextColor(getResources().getColor(R.color.green));
                correctAnswer++;


            } else {
                binding.ansFeedbackTv.setTextColor(getResources().getColor(R.color.red));
                binding.ansFeedbackTv.setText("Wrong Answer \nCorrect Answer is :" + answer);
                button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_wrong));
                wrongAnswer++;

            }
        }
        canAnswer = false;
        countDownTimer.cancel();
        showNextQuestion();
    }

    private void showNextQuestion() {
        if (currentQuestionNo == totalQueCount) {
            binding.nextQueBtn.setText("Submit");
            binding.nextQueBtn.setVisibility(View.VISIBLE);
            binding.nextQueBtn.setEnabled(true);
            binding.ansFeedbackTv.setVisibility(View.VISIBLE);

        } else {
            binding.nextQueBtn.setVisibility(View.VISIBLE);
            binding.nextQueBtn.setEnabled(true);
            binding.ansFeedbackTv.setVisibility(View.VISIBLE);
        }
    }
}