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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.iesmaster.model.Questions;
import com.example.iesmaster.R;



public class QuestionFragment extends Fragment {
    TextView Question,lblQuestion,txtMarks,Answer;
    WebView webQuestion, webAnswer;
    ImageView questionImage;
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

    //io.github.kexanie.library.MathView mathQuestion;
    katex.hourglass.in.mathlib.MathView katexQuestion;
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
        questionImage = fragmentView.findViewById(R.id.questionImage);
        //Answer = fragmentView.findViewById(R.id.Answer);
        lblQuestion.setText("Question " + Integer.toString(QNo) );

        katexQuestion = fragmentView.findViewById(R.id.katexQuestion);

       katexQuestion.setDisplayText(questionData.Questions);      //setText(questionData.Questions);

      /*  try {
            String sampleData = "\"This come from string. You can insert inline formula:\" +\n" +
                    "            \" \\\\(ax^2 + bx + c = 0\\\\) \" +\n" +
                    "            \"or displayed formula: $$\\\\sum_{i=0}^n i^2 = \\\\frac{(n^2+n)(2n+1)}{6}$$\";";

          mathQuestion = fragmentView.findViewById(R.id.mathQuestion);
            mathQuestion.config(
                    "MathJax.Hub.Config({\n"+
                            "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                            "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                            "         SVG: { linebreaks: { automatic: true } }\n"+
                            "});");
           mathQuestion.setText(questionData.Questions);
        }
        catch (Exception ex)
        {
            int a=1;
        }*/
          webQuestion = fragmentView.findViewById(R.id.webQuestion);
      //    webQuestion.getSettings().setJavaScriptEnabled(true);
       //   SetWebViewKatex(webQuestion, questionData.Questions);
      // SetWebViewNew(webQuestion, questionData.Questions);


      //  SetWebView(webQuestion,"file:///android_asset/questions.html");

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

    private void SetWebViewKatex(final WebView webView, final  String data)
    {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        String references = "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/katex@0.10.2/dist/katex.min.css\" />"
                          + "<script src=\"https://cdn.jsdelivr.net/npm/katex@0.10.2/dist/katex.min.js\" crossorigin=\"anonymous\"></script>"
                 + "<span id='math'></span>";
        webView.loadDataWithBaseURL(references, data, "utf-8", "","");


        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                webView.loadUrl("javascript:document.getElementById('math').innerHTML='\\\\["
                        +data
                        +"\\\\]';");

                webView.setVisibility(View.VISIBLE);
            }
        });


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


    private void SetWebViewNew(final WebView webView, final  String data)
    {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadDataWithBaseURL("http://bar", "<script type='text/x-mathjax-config'>"
                + "MathJax.Hub.Config({ "
                + "showMathMenu: false, "
                + "jax: ['input/TeX','output/HTML-CSS'], "
                + "extensions: ['tex2jax.js'], "
                + "TeX: { extensions: ['AMSmath.js','AMSsymbols.js',"
                + "'noErrors.js','noUndefined.js'] } "
                + "});</script>"
                + "<script type='text/javascript' "
                + "src='file:///android_asset/MathJax/MathJax.js'"
                + "></script><span id='math'></span>", "text/html", "utf-8", "");


        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                webView.loadUrl("javascript:document.getElementById('math').innerHTML='\\\\["
                        +data
                        +"\\\\]';");

                webView.setVisibility(View.VISIBLE);
            }
        });


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
