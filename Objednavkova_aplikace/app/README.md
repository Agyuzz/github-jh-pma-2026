## Trvalé ukládání objednávek

### Výchozí stav
Objednávka se v aplikaci vytvoří na hlavní obrazovce (verze hry, příplatky, cena)
a na obrazovku rekapitulace se předává přes Intent. Data tak existují jen v paměti
a po zavření aplikace zmizí. Cílem je ukládat objednávky trvale, tedy aby
přežily zavření aplikace i restart telefonu.

### Prozkoumané možnosti

**1. Room (SQLite databáze)**
Knihovna od Googlu nad databází SQLite, která je součástí Androidu. Objednávka se
popíše jako datová třída (`@Entity`), operace se definují v rozhraní `@Dao`
(vložit, načíst, smazat) a data se ukládají do souboru databáze v soukromém úložišti
aplikace. Výhodou jsou strukturovaná data, dotazy (řazení, filtrování) a oficiální
podpora Googlu. Nevýhodou je trochu víc úvodního nastavení (závislosti, třídy).

**2. DataStore**
Moderní náhrada za SharedPreferences, která ukládá dvojice klíč–hodnota. Hodí se pro
malé množství dat, např. nastavení aplikace. Pro seznam více objednávek je nevhodný,
protože nemá dotazy a nepočítá se strukturou záznamů.

**3. Soubor (JSON/CSV)**
Objednávky se zapíšou do textového souboru ve vnitřním úložišti aplikace. Řešení je
jednoduché a bez knihoven, ale chybí dotazování a ochrana při chybě zápisu a při
větším počtu objednávek se soubor musí pokaždé celý načítat.

**4. Online databáze (např. Firebase Firestore)**
Data se ukládají na server a jsou dostupná z více zařízení. Vyžaduje to internet,
účet a nastavení projektu, což je pro tuto aplikaci zbytečně složité.

V případě Room - Objednávka by byla uložena jako entita:

```kotlin
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameVersion: String,
    val totalPrice: Int,
    val hasGameTime: Boolean,
    val hasBoost: Boolean,
    val hasMount: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)
```

Při potvrzení objednávky na obrazovce rekapitulace by se objekt `Order` uložil
do databáze přes DAO. Díky tomu by byl dostupný i po restartu aplikace a bylo
by možné vytvořit například obrazovku s historií objednávek.