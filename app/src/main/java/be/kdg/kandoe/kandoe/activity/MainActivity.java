package be.kdg.kandoe.kandoe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.adapter.CustomPagerAdapter;
import be.kdg.kandoe.kandoe.dom.User;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Drawer drawer;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initTabLayout(); --> needed?
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViewPager();
        initMaterialDrawer();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(),CustomPagerAdapter.mNumOfTabs);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if(drawer != null && drawer.isDrawerOpen())
            drawer.closeDrawer();
        else
            super.onBackPressed();
    }
    private void initMaterialDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Profile");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Organisations");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("Extra Option");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("Extra Option");

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_background1)
                .withAlternativeProfileHeaderSwitching(false)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withIsExpanded(false)
                                .withName("name here")
                                .withEmail("emailhere.com")
                                .withEnabled(false)
                                .withIcon(new IconDrawable(this, FontAwesomeIcons.fa_reddit))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {

                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("111111111").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out)),
                        new PrimaryDrawerItem().withName("111111111").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out)),
                        new PrimaryDrawerItem().withName("111111111").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out)),
                        new SectionDrawerItem().withName("SECTION"),
                        new SecondaryDrawerItem().withName("22222").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out)),
                        new SecondaryDrawerItem().withName("22222").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out)),
                        new SecondaryDrawerItem().withName("22222").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out)),
                        new SecondaryDrawerItem().withName("22222").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(MainActivity.this, ((Nameable) drawerItem).getName().getText(MainActivity.this), Toast.LENGTH_SHORT).show();
                        }
                        //true: do nothing
                        //false: close drawer
                        return true;
                    }
                }).build();

        drawer.addStickyFooterItem(new PrimaryDrawerItem()
                        .withName("Log out")
                        .withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out).alpha(100)));

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }
}