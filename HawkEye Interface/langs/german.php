<?php

	///////////////////////////////////////////////////
	//         HawkEye Interface Lang File           //
	//                 by oliverw92                  //
	///////////////////////////////////////////////////
	//      German Lang File by untergrundbiber      //
	///////////////////////////////////////////////////	
	$lang = array(
					
					"pageTitle"  => "HawkEye Browser",
					"title" => "HawkEye",
					
					"filter" => array("title" => "Filter-Optionen",
									  "players" => "Spieler",
									  "xyz" => "XYZ",
									  "range" => "Reichweite",
									  "keys" => "Stichwoerter",
									  "worlds" => "Welten",
									  "servers" => "Server",
									  "dFrom" => "Von Datum",
									  "dTo" => "Bis Datum",
									  "block" => "Block",
									  "search" => "Suche",
									  "exclude" => "Ausschluss-Filter",
									  "selectall" => "Alles auswaehlen"),
					
					"tips" => array("hideFilter" => "Zeige / Verstecke Filter-Optionen",
									"hideResults" => "Zeige / Verstecke Ergebnisse",
									"actions" => "Aktionen die du suchen willst. Es muss mind. eine ausgewaehlt werden.",
									"password" => "Passwort um die Suche zu benutzen. Wird nur gebraucht wenn gesetzt.",
									"players" => "(Optional) Liste von Spielern, nach denen gesucht werden soll, getrennt durch Kommas.",
									"xyz" => "(Optional) Koordinaten, in deren Umkreis du suchen willst",
									"range" => "(Optional) Suchreichweite um die Koordinaten",
									"keys" => "(Optional) Liste von Stichworten, getrennt durch Kommas.",
									"worlds" => "(Optional) Liste der Welten, getrennt durch Kommas. Leeres Feld entspricht alle Welten",
									"servers" => "(Optional) Liste der Server, getrennt durch Kommas. Leeres Feld entspricht alle Server",
									"dFrom" => "(Optional) Start Zeit und Datum fuer Suchzeitraum",
									"dTo" => "(Optional) Ende Zeit und Datum fuer Suchzeitraum",
									"block" => "(Optional) Block nach dem gesucht wird bei 'Block zerstoert' und 'Block platziert'",
									"reverse" => "Wenn diese Option aktiviert ist, wird der Log in chronologischer Reihenfolge angezeigt. Deaktiviere die Option zum Anzeigen von Chat-Protokollen",
									"exclude" => "(Optional) Liste der Stichworte, die aus der Suche ausgeschlossen werden sollen, getrennt durch Kommas.",
									"selectall" => "Klicke hier, um alle Aktionen an- oder abzuwaehlen"),
					
					"actions" => array("0" => "Block zerstoert",
									   "1" => "Block plaziert",
									   "2" => "Schild platziert",
									   "3" => "Chat",
									   "4" => "Kommando",
									   "5" => "Login",
									   "6" => "Logout",
									   "7" => "Teleport",
									   "8" => "Lava-Eimer",
									   "9" => "Wasser-Eimer",
									   "10" => "Kiste geoeffnet",
									   "11" => "Tuer benutzt",
									   "12" => "Tod durch PVP",
									   "13" => "Feuerzeug",
									   "14" => "Hebel benutzt",
									   "15" => "Taste benutzt",
									   "16" => "Sonstiges",
									   "17" => "Explosion",
									   "18" => "Feuer",
									   "19" => "Block entsteht",
									   "20" => "Blaetter-Zerfall",
									   "21" => "Tod durch Mob",
									   "22" => "Sonstiger Tod",
									   "23" => "Item gedroppt",
									   "24" => "Item aufgehoben",
									   "25" => "Block verschwindet",
									   "26" => "Lavafluss",
									   "27" => "Wasserfluss",
									   "28" => "Kisten-Aktion",
									   "29" => "Schild zerstoert",
									   "30" => "Bild zerstoert",
									   "31" => "Bild platziert",
									   "32" => "Enderman aufgehoben",
									   "33" => "Enderman platziert",
									   "34" => "Baumwuchs",
									   "35" => "Pilzwuchs",
									   "36" => "Mob Kill",
									   "37" => "Spawn Egg",
									   "38" => "HeroChat",
									   "39" => "Entity geaendert",
									   "40" => "Block-Bewohner geaendert",
									   "41" => "Super-Pickaxe",
									   "42" => "WorldEdit-Zerstoert",
									   "43" => "WorldEdit-Plaziert",
									   "44" => "Feld zertreten",
                                       "45" => "Block entzündet",
									   "46" => "Fallender Block platziert"),
					
					"results" => array("title" => "Ergebnisse",
									   "id" => "ID",
									   "date" => "Datum",
									   "player" => "Spieler",
									   "action" => "Aktion",
									   "world" => "Welt",
									   "server" => "Server",
									   "xyz" => "XYZ",
									   "data" => "Daten"),
					
					"login" => array("password" => "Passwort: ",
									 "login" => "Login"),
					
					"messages" => array("clickTo" => "Klicke auf Suche um Ergenisse zu erhalten",
										"breakMe" => "Mach mich nicht kaputt!",
										"invalidPass" => "Falsches Passwort!",
										"noActions" => "Du musst mind. eine Aktion auswaehlen nach der gesucht werden soll!",
										"noResults" => "Keine Ergebnisse gefunden mit dieser Auswahl",
										"error" => "Fehler!",
										"notLoggedIn" => "Du bist nicht angemeldet!")
					);
	
	//Convert foreign characters to entities
	array_walk_recursive($lang, "ents");
	function ents(&$item, $key) {
		$item = htmlentities($item);
	}
?>