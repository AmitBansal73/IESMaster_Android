package com.example.iesmaster.Questions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Scene;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.iesmaster.Object.Questions;
import com.example.iesmaster.R;

public class QuestionFragment extends Fragment {
    TextView Question,lblQuestion,txtMarks,Answer;
    WebView webQuestion, webAnswer;
    ProgressBar progressBar;
    private static Questions questionData;
    private int mPageNumber;
    public static int size, QNo;
    public static final String ARG_PAGE = "page";
    private OnFragmentInteractionListener mListener;

    Scene QuestionScene;
    Scene AnswerScene;

    ViewFlipper viewFlippper;

    TextView btnShowAnswer, btnShowQuestion;

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup fragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_question, container, false);

        viewFlippper = fragmentView.findViewById(R.id.viewFlipper);
         //Question = fragmentView.findViewById(R.id.webViewQuestion);
          lblQuestion = fragmentView.findViewById(R.id.lblQuestion);
        txtMarks = fragmentView.findViewById(R.id.txtMarks);
        //Answer = fragmentView.findViewById(R.id.Answer);
        lblQuestion.setText("Question " + Integer.toString(QNo) );


        webQuestion = fragmentView.findViewById(R.id.webQuestion);
        SetWebView(webQuestion,"file:///android_asset/questions.html");

        webAnswer = fragmentView.findViewById(R.id.webAnswer);
        SetWebView(webAnswer,"file:///android_asset/answer.html");

        txtMarks.setText("Marks 10");
        btnShowAnswer = fragmentView.findViewById(R.id.btnShowAnswer);
        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlippper.setFlipInterval(2000);
                viewFlippper.showPrevious();
                //viewFlippper.stopFlipping();
            }
        });


        btnShowQuestion = fragmentView.findViewById(R.id.btnShowQuestion);
        btnShowQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlippper.setFlipInterval(2000);
                viewFlippper.showPrevious();
                //viewFlippper.stopFlipping();
            }
        });



        return fragmentView;
    }


    private void SetWebView(final WebView webView,  String url)
    {

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);


        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(),"Long Click Disabled",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        webView.setLongClickable(false);

        Resources res = getResources();
        float fontSize = res.getDimension(R.dimen.txtSize8);
        webView.getSettings().setDefaultFontSize((int)fontSize);
    }



    static QuestionFragment newInstance(int position, Questions ques, int count){
        try {
            QNo = position+1;
            QuestionFragment swipeFragment = new QuestionFragment();
            questionData= ques;
            size = count;
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, position);
            swipeFragment.setArguments(args);
            return swipeFragment;
        }
        catch (Exception ex)
        {

            return  null;
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
