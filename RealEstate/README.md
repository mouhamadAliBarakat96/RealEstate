Project Stories.


Done :
*****
1-realEstate list and card
2-add calls and views
3-navigate to whatsapp via click call
4-error page
5-Chalet list in homepage with filters
6-Chalet-card
7-page list of real estate and chalet.
8-create new Post (chalet , post)
To Do:
*****
-search in select menu
-add WaterResources in card.
-Sing In & Sign Up (front-end)

 
 
Arabic Websites
***************
-template styles and redirect bundle to arabic

To Discusion:
*************
-contact us


New Stories
***********
1-badges (isVerified, isBroker , boosted)


4-problem in redirect after login to current url
5-problem in pagination in index page
6-probleme bundle not show more than one message if we have more

- suwar widthh 
-google map



7-login page design (barakat) 
8-sign up page to create (barakat)
9-ads 
 
 
 
 
 
 
 
 
 
 
 Added By Mohamd Ali 
 **************
 
 post condition


// hayde yaane 3adad el suwar

	if (inputParts.size() > Constants.NB_IMAGE_IN_POST_ALLOWED) {
				return Response.status(Status.BAD_REQUEST).entity(Constants.NB_OF_IMAGE_GREATER_NUMBER_OF_IMAGE_ALLOWED)
						.build();

			}

// hayde 3adad el poset lal user
// mana lal challet





	if (appratmentRent.getSpace() < 50) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_50");

			}
			if (appratmentRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}


	if (appratmentSell.getSpace() < 50) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_50");

			}
			if (appratmentSell.getSpace() * 100 < appratmentSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}





if (land.getSpace() < 200) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_200");

			}
			if (land.getSpace() * 4 < land.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_4_DOLLARS");
			}


if (shopRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			if (shopRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}


	if (shopSell.getSpace() * 100 < shopSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}
			if (shopSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}


	if (officeRent.getPrice() < 60) {
				throw new Exception("PRICE_OF_RENT_SHOULD_BE_GREATER_60");
			}
			if (officeRent.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}

if (officeSell.getSpace() * 100 < officeSell.getPrice()) {
				throw new Exception("PRICE_OF_METER_SHOULD_BE_GREATER_THEN_100_DOLLARS");
			}
			if (officeSell.getSpace() < 40) {
				throw new Exception("SPACE_SHOULD_BE_GREATER_40");

			}

realEstate.setPostDate(new Date());

realEstate.setPostStatus(PostStatus.PENDING);


w lal chalet 

chalet.setPostDate(new Date());

chalet.setPostStatus(PostStatus.PENDING);


/*
here in post */


private User checkUserConstraint(User user ) throws Exception {

		

		

		

		if (!postType.equals("CHALET")) {
			Long nbOfPost = restateFacade.findUserCountPostPendingOrActive(user.getId());

			// nb of post allowed ;
			// krml is broker bytla3lo zyde
			int nbOfPostAllowed = 0;
			if (user.isBroker()) {
				nbOfPostAllowed = appSinglton.getBrokerNbOfPost();
			}

			if (user.getUserCategory() == UserCategory.REGULAR) {
				nbOfPostAllowed = nbOfPostAllowed + appSinglton.getFreeNbOfPost();

			} else if (user.getUserCategory() == UserCategory.MEDUIM) {

				nbOfPostAllowed = nbOfPostAllowed + appSinglton.getMeduimAccountNbOfPost();

			} else if (user.getUserCategory() == UserCategory.PREMIUM) {
				nbOfPostAllowed = nbOfPostAllowed + appSinglton.getPremuimAccountNbOfPost();
			}

			if (nbOfPost >= nbOfPostAllowed) {
				throw new Exception("EXCEEDED_POST_LIMIT");

			}

		}

		return user;

	}
	
	
	
	if post verfied n7otolo badge 
	
	@Expose
	private boolean isVerfied;
	


iza user maktab 3akre nhotolo shi
bel classs User  
	private boolean isBroker;


enta w 3am tkhod profile picture aw suwar 3an l post t2kd enon 
		String[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif" }; // Add more image extensions if needed
 enta bthoton hol b compomnent l jsf 
 
 
 bel realestat w bel chalet
 sar fi boolean isBoosted izA HAYDA TRUE BADAK TBYN ENO HYDA EL POST MAD3OM ENO MA3MLO SUPONSER
 
 
 // newew
 
 fix l suwar li 3al wejha --done
lzm ykon ma2ston ad baad --done


bel  search el asasye li bel home bath rom w bed roums ma ybyno ila iza khtar l fieldet li lkhason b he kshi  --done
--
lh manak 3mil padding lal safha lzm ykon el jawenib white  --to discusion with you

 /******
/
new  userPost-card badak tkbos marten 3ala kabset el save la  trouh ( aw2t 3am t3ale2)


by default kon mkhtar whde menon whde  lal apprement for rent aw sale hol

fo2 l map hot eno lzm nkon mkhtarin shi khaso bel map 




/***************Finish Working After Viewss*******************/

ملاحظات للتعديل :

1 مكان رقم 0 وضع كلمة لا يوجد --done
2-رقم الطابق (سفلي- اول- ثاني ....) --done
3-عند الاضافة زيادة القضاء و المحافظة
4-ضمن الشاليه تعديل الاسم الى سعر ايام الاسبوع بدل ايام الاسبوع   --done

ملاحظات جديدة :
1-login tkun lahala button--done
2-register fena nshela kawno mn 5elel login --done
3-add insta fb playstore appstore
4-remark location opened LINKS NAVIGHATION  --done
5-add search ON select
6-default value in select "Any"  --done 
7-bedrooms w ath roos count enum 1 to 7+ multin select
8-rent-sale-all in select menu before property type
9-pagiantion fix 10 w next tkun button style
10-bl add new filter property type ykun fe all rent sale
11-subtitle---> description in addd new
12-feedback by user


















