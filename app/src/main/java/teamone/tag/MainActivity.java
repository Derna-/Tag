package teamone.tag;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
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

public class MainActivity extends ActionBarActivity implements View.OnClickListener, CameraBridgeViewBase.CvCameraViewListener2 {

    private Toolbar toolbar;
    private static  final  String TAG_BUTTON_ONE = "_buttonOne_";
    private static  final  String TAG_BUTTON_TWO = "_buttonTwo_";
    private static  final  String TAG_BUTTON_THREE = "_buttonThree_";
    private static  final  String TAG_BUTTON_FOUR = "_buttonFour_";
    private static  final  String TAG_BASE_LOADER_CALLBACK = "_baseLoaderCallback_";


    private CameraBridgeViewBase javaCameraView;
    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG_BASE_LOADER_CALLBACK, "OpenCV loaded successfully");
                    javaCameraView.enableView();

                }
                default:
                {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout),toolbar );
        //build boutton flottant
        buildFAB();
        //opencv cameraview, voir fichier activity_main.xml
        javaCameraView = (CameraBridgeViewBase) findViewById(R.id.cameraView);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

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
        ImageView soundIco = new ImageView(this);
        soundIco.setImageResource(R.drawable.ic_action_headphones);
        ImageView imageIco = new ImageView(this);
        imageIco.setImageResource(R.drawable.ic_action_picture);


        //setthe background for all the sub buttons
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_acred));


        //build the sub buttons
        SubActionButton button1 = itemBuilder.setContentView(cameraIcon).build();
        button1.setTag(TAG_BUTTON_ONE);
        button1.setOnClickListener(this);
        SubActionButton button2 = itemBuilder.setContentView(imageIco).build();
        button2.setTag(TAG_BUTTON_TWO);
        button2.setOnClickListener(this);
        SubActionButton button3 = itemBuilder.setContentView(textIcon).build();
        button3.setTag(TAG_BUTTON_THREE);
        button3.setOnClickListener(this);
        SubActionButton button4 = itemBuilder.setContentView(soundIco).build();
        button4.setTag(TAG_BUTTON_FOUR);
        button4.setOnClickListener(this);




        //add sub buttons to the main floating action button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)

                .attachTo(rightLowerButton)
                .build();
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
     * sert a desactiver la camera quand l'activty est terminée
     * juste au K.O
     */
    public void onDestroy(){
        super.onDestroy();
        if (javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tindin) {
            //Toast.makeText(this, "Test Toast Settings : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        }

        if(id==R.id.developers) {
            startActivity(new Intent(this, Developpeurs.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag().equals(TAG_BUTTON_ONE)){

        }
        if(v.getTag().equals(TAG_BUTTON_TWO)){

        }
        if(v.getTag().equals(TAG_BUTTON_THREE)){

        }
        if(v.getTag().equals(TAG_BUTTON_FOUR)){

        }

    }


    /**
     * methode de l'interface CvCameraViewListener2
     * @param width -  the width of the frames that will be delivered
     * @param height - the height of the frames that will be delivered
     */
    @Override
    public void onCameraViewStarted(int width, int height) {

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
        //return inputFrame.rgba();

        //TODO - a voir, c'est pas terrible
        Mat mRgba = inputFrame.rgba();
        Mat mRgbaT = mRgba.t();
        Core.flip(mRgba.t(), mRgbaT, 1);
        Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());
        return mRgbaT;
    }


    @Override
    public void onPause()
    {
        super.onPause();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }
}
