package teamone.tag;

import java.util.List;
import java.util.ListIterator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.graphics.Bitmap;

import android.hardware.Camera.Size;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;

import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;



import android.content.Intent;
import android.hardware.Camera;

import android.support.v7.app.ActionBarActivity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import android.widget.ImageView;
import android.widget.Toast;
//Bouton flottant bas droit
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
//Computer Vision Library
import org.opencv.android.BaseLoaderCallback; //Listener Object
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;     //Camera Handler
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;       //Bind OpenCV to Activity
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener {

    private Toolbar toolbar;
    private static  final  String TAG_BUTTON_CAMERA = "_buttonCamera_";
    private static  final  String TAG_BUTTON_IMAGE = "_buttonImage_";
    private static  final  String TAG_BUTTON_TEXT = "_buttonText_";
    private static  final  String TAG_BUTTON_SOUND = "_buttonSound_";
    private static  final  String TAG_BASE_LOADER_CALLBACK = "_baseLoaderCallback_";
    private static  final  String TAG_CLASS_CONSTRUCTOR ="_class_MainActivity_";
    private static  final  String TAG="_app::Activity_";
    private CameraView mOpenCvCameraView;
    private List<Size> mResolutionList;
    private MenuItem[] mResolutionMenuItems;
    private SubMenu mResolutionMenu;
    private static final int GRID_SIZE = 4;
    private static final int GRID_AREA = GRID_SIZE * GRID_SIZE;
    private Mat[] mCells;
    private Mat mRgba;
    private Bitmap mPicture;
    private CameraView mView;



    //private CameraBridgeViewBase javaCameraView;


    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG_BASE_LOADER_CALLBACK, "OpenCV loaded successfully");
                    //javaCameraView.enableView();
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);

                } break;
                default:
                {
                    super.onManagerConnected(status);
                }  break;
            }
        }
    };
    private int width;
    private int height;


    public MainActivity() {
        //Log.i(TAG_CLASS_CONSTRUCTOR, "Instantiated new " + this.getClass());
        //Log.i(TAG_CLASS_CONSTRUCTOR, "Instantiated new " + this.getLocalClassName());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout),toolbar );
        //build boutton flottant
        buildFAB();
        //opencv cameraview, voir fichier activity_main.xml
        //javaCameraView = (CameraBridgeViewBase) findViewById(R.id.cameraView);
        //javaCameraView.setVisibility(SurfaceView.VISIBLE);
        //javaCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView = (CameraView) findViewById(R.id.activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        final TextView textViewToChange = (TextView) findViewById(R.id.textVersion);
        textViewToChange.setText(
                "alpha 3 E" );



    }

    /**
     * Construction du bouton flotant
     */
    private void buildFAB() {

        final ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageResource(R.drawable.ic_action_new);
        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .setBackgroundDrawable(R.drawable.button_action_acred)
                .build();

        ImageView cameraIcon = new ImageView(this);
        cameraIcon.setImageResource(R.drawable.ic_action_camera);
        ImageView textIcon = new ImageView(this);
        textIcon.setImageResource(R.drawable.ic_action_chat);
        ImageView soundIcon = new ImageView(this);
        soundIcon.setImageResource(R.drawable.ic_action_headphones);
        ImageView imageIcon = new ImageView(this);
        imageIcon.setImageResource(R.drawable.ic_action_picture);


        //setthe background for all the sub buttons
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_acred));


        //build the sub buttons
        SubActionButton cameraIconButton = itemBuilder.setContentView(cameraIcon).build();
        cameraIconButton.setTag(TAG_BUTTON_CAMERA);
        cameraIconButton.setOnClickListener(this);
        SubActionButton imageIcoButton = itemBuilder.setContentView(imageIcon).build();
        imageIcoButton.setTag(TAG_BUTTON_IMAGE);
        imageIcoButton.setOnClickListener(this);
        SubActionButton textIconButton = itemBuilder.setContentView(textIcon).build();
        textIconButton.setTag(TAG_BUTTON_TEXT);
        textIconButton.setOnClickListener(this);
        SubActionButton soundIconButton = itemBuilder.setContentView(soundIcon).build();
        soundIconButton.setTag(TAG_BUTTON_SOUND);
        soundIconButton.setOnClickListener(this);




        //add sub buttons to the main floating action button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(cameraIconButton)
                .addSubActionView(imageIcoButton)
                .addSubActionView(textIconButton)
                .addSubActionView(soundIconButton)

                .attachTo(rightLowerButton)
                .build();
    }




    @Override
    public void onPause()
    {
        super.onPause();
        //if (javaCameraView != null)
        //    javaCameraView.disableView();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    /**
     * Lancer apres que l'activity sera crée mais avant qu'elle sera afficher pour l'utilisateur
     * va servir pour initier la bibliotheque opencv
     */
    @Override
    public void onResume(){
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9,this, baseLoaderCallback );

    }

    /**
     * sert a desactiver la camera quand l'activity est terminée
     * juste au K.O
     */
    public void onDestroy(){
        super.onDestroy();
        //if (javaCameraView != null) {
        //    javaCameraView.disableView();
        //}
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // recuperer les résolution possible puis l'afficher

        mResolutionMenu = menu.addSubMenu("Resolution");
        mResolutionList = mOpenCvCameraView.getResolutionList();


        mResolutionMenuItems = new MenuItem[mResolutionList.size()];

        ListIterator<Size> resolutionItr = mResolutionList.listIterator();
        int idx = 0;
        while(resolutionItr.hasNext()) {
            Size element = resolutionItr.next();
            mResolutionMenuItems[idx] = mResolutionMenu.add(2, idx, Menu.NONE,
                    Integer.valueOf(element.width).toString() + "x" + Integer.valueOf(element.height).toString());
            idx++;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tindin) {
            //Toast.makeText(this, "Test Toast Settings : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        }

        if(id==R.id.developers) {
            startActivity(new Intent(this, Developpeurs.class));
        }

        else if (item.getGroupId() == 2)
        {
            Size resolution = mResolutionList.get(id);
            mOpenCvCameraView.setResolution(resolution);
            resolution = mOpenCvCameraView.getResolution();
            String caption = Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
            Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag().equals(TAG_BUTTON_CAMERA)){
        //TODO test sift camera
            Toast.makeText(this, "avant", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ImageProcess.class));
            Toast.makeText(this, "apres", Toast.LENGTH_SHORT).show();
        }
        if(v.getTag().equals(TAG_BUTTON_IMAGE)){
            startActivity(new Intent(this, ImageChooser.class));
        }
        if(v.getTag().equals(TAG_BUTTON_TEXT)){

        }
        if(v.getTag().equals(TAG_BUTTON_SOUND)){

        }

    }


    /**
     * methode de l'interface CvCameraViewListener2
     * @param width -  the width of the frames that will be delivered
     * @param height - the height of the frames that will be delivered
     */
    @Override
    public void onCameraViewStarted(int width, int height) {
        this.width = width;
        this.height = height;

    }

    /**
     * methode de l'interface CvCameraViewListener2
     */
    @Override
    public void onCameraViewStopped() {

    }

    /**
     * methode de l'interface CvCameraViewListener2
     * elle est appeler quand la camera va nous envoyer un frame (une image)
     * avant qu'elle sera appeler les données du camera "se converti" ver le type CvCameraViewFrame
     *      CvCameraViewFrame : c'est juste une collection des objets mat (matrice)
     *      +rgba : Mat   // RGB + alpha data
     *      +gray : Mat   // grayscale data
     * @param inputFrame
     * @return
     */
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
        //TODO - a voir, c'est pas terrible
        //Mat mRgba = inputFrame.rgba();
        //Mat mRgbaT = mRgba.t();
        //Core.flip(mRgba.t(), mRgbaT, 1);
        //Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());
        //return mRgbaT;
        //return mRgbaT;
    }


    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i(TAG,"onTouch event");
        //Toast.makeText(this, "Screen_TouchEvent", Toast.LENGTH_SHORT).show();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        //String currentDateandTime = sdf.format(new Date());
        //String fileName = Environment.getExternalStorageDirectory().getPath() +
        //        "/sample_picture_" + currentDateandTime + ".jpg";
        //mOpenCvCameraView.takePicture(fileName);
        //Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();
        return false;
    }

}
