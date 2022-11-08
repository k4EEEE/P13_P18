```diff
# P13_P18
APDU Generate AC for P13 P18
BRACELET	P18
====================
Terminals Connected 
1.Terminal : PC/SC terminal BROADCOM NFC Smartcard Reader 0<br>
2.Terminal : PC/SC terminal Gemalto Prox-DU Contact_10800095 0<br>
3.Terminal : PC/SC terminal Gemalto Prox-DU Contactless_10800095 0<br>
card: PC/SC card in Gemalto Prox-DU Contactless_10800095 0, protocol T=1, state OK<br>
Card present!<br>
<br>
@ATR: 3B8F80018031E06B04520502FD55555555555598<br>
<br>
@Read PPSE answer:<br> 6F49840E325041592E5359532E4444463031A537BF0C3461124F07A000000228101050046D616461870101611E4F07A000000004101050104465626974204D6173746572436172648701029000<br>
<br>
@@MADA answer: 6F448407A0000002281010A53950046D6164618701019F38039F40055F2D046172656E9F1101019F120A6D616461204465626974BF0C0F9F4D020B0A9F6E07068200003030009000<br>
<br>
@@MADA GPO: 771282021980940C1001020218010200200102009000<br>
<br>
@@READRECORD_21 answer: 70818A5F25032211065F24032510315A0855856308474619325F3401025F280206825F300202019F0702AB809F080200029F420206829F4401029F0D05FC60BC88009F0E0500100000009F0F05FC68BC98008C279F02069F03069F1A0295055F2A029A039C019F37049F35019F45029F4C089F34039F21039F7C148D0C910A8A0295059F37049F4C089F4A01829000<br>
<br>
@@READRECORD_31 answer: 70078F01339F3201039000<br>
<br>
@@READRECORD_32 answer: 7081FB9081F846F9D75D554F6391C5F347658F6E27C2CFEB72A5F3C797F117CF63C15BD3E22E2F27061819C0F96F9F6F82F226C383D92D89BEC38609E2CE2F3F89D4FE141BB410037D4FECCF6F0BFD6B66B7B88E612951465B237CF1A9179C577DEB24CB89DEA9D3EF317DC84628E981EEE8607ECC46AF6E8B47A55EC590511F5A6BB7FF7E6E2DCDC3739A5A54E3D1DB09856E66743EBD9DEAB4803A65E0FD2F3D4AD7BE390C1492C94E626EF80ABF6906B7536BA92FDDE109705855D5AE0187F8484AE67749A6EBA5F74D2E0B6D1B170D6F13AC2042C81234A28A5C8CC1F4D0FEBEEC62FA66E2658C95DC2F7EAC4BF32F23A020CEBD504AEEF10CF7FC589000<br>
<br>
@@READRECORD_41 answer: 70179F4701039F480A86A109F9FC8965C688EF9F49039F37049000<br>
<br>
@@READRECORD_42 answer: 7081B49F4681B0373CDA8A3F0606F0902D73A7594A87439FE689A2AB0DBF367B77507AED07666EA3A9A76B21F58BF59E7586991EEC8E78A713152E48E37EFCF08900E9CC60B2C551F564199E5518BBADBA798E7B52E449D291CDA5D930A0D51998E3319A9804AF442419C2BBB8A4E987E19EB4B7B3CBC06B968CADB522599C45A8DC27AAE425B21356B4532B4501CDAA10D1083EEA77CD496BF596430A07A9FCADC95BE5D45DBF8657F39EBC087E83405CD365F4CEA7669000<br>
<br>
+GEN AC COMMAND: 80AE800042000000001100000000000000068200800080000682221108003357A30B22000000000000000000001F0302020202000000000000000000000000000000000000000000<br>
<br>
!Generate AC answer: 77299F2701809F3602001A9F2608A72398CD8DC364BA9F10120110A00103A20000000000000000000000FF9000<br>
<br>
-ARQC : 9F2608A72398CD8DC364BA<br>
<br>
Card Disconnected<br>


<br>
# **BRACELET	P13**
====
Terminals Connected <br>
1.Terminal : PC/SC terminal BROADCOM NFC Smartcard Reader 0<br>
2.Terminal : PC/SC terminal Gemalto Prox-DU Contact_10800095 0<br>
3.Terminal : PC/SC terminal Gemalto Prox-DU Contactless_10800095 0<br>
card: PC/SC card in Gemalto Prox-DU Contactless_10800095 0, protocol T=1, state OK<br>
Card present!<br>
<br>
**ATR:** 3B8F80018031E06B04310502F0555555555555F6<br>
<br>
**Read PPSE answer:** 6F29840E325041592E5359532E4444463031A517BF0C1461124F07A000000228101050046D6164618701019000<br>
<br>
**MADA answer:**<br> 6F468407A0000002281010A53B50046D6164618701019F38039F40055F2D046172656E9F1101019F120C6D6164612050726570616964BF0C0F9F4D020B0A9F6E07068200003030009000<br>
<br>
**MADA GPO:** 7716820219809410100202011801020020010100200303009000<br>
<br>
**READRECORD_21 answer:** 7081CB57129682090865031295D231160000000915001F5A0896820908650312955F3401025F201A4748414C412F414C414A4D4920202020202020202020202020205F24032311305F25032110195F300206005F280206829F0702AB809F080200029F420206829F4401028C279F02069F03069F1A0295055F2A029A039C019F37049F35019F45029F4C089F34039F21039F7C148D0C910A8A0295059F37049F4C088E0E00000000000000000205440302009F0D05FC60BC88009F0E0500100000009F0F05FC68BC98009F4A01829000<br>
<br>
**READRECORD_31 answer:** 70078F01299F3201039000<br>
<br>
**READRECORD_32 answer:** 7081FB9081F81DB94B2F4588C9B85729E18ED6558513B1B1396F72B46A672027571355259B4695EB8C74AE0EA4858FA8675F1EDC8696C8A9F343FD64D6FDCE9A523A13A06161E804A9A97942C0DC5CF9966058FEDFCA797B251B7FA9BB1760514B8614AA8E08060B99346925EE5FE3845C1548DA0D9C72BAA33A8BB1E6CF8C5F7B1A43EBFEF19F59CF50F695CF88EE647C49DFA24C3934F7E9C258A2DEF6D4CD3E1E6FA7FD0F9550CA7FBA0AD3405CCCA2A4B1D05499CBCBE59C5264B7A8373E154F283D1737ABE00B6C3FE4B06CB2783F861F88346BCE0CA8F64ABD3BBC618F63A16F98B83784BC15556D335B7B8F999DC07690F3891FF3C86A732CE0B09000<br>
<br>
**READRECORD_41 answer:** 70379F49039F37049F480AE4C27943C98193D4509D00000000000000000000000000000000000000000000000000000000000000009F4701039000<br>
<br>
**READRECORD_42 answer:** 7081B49F4681B06412A8444B56DC37E37F71E1AE7E2AF41D27480600E1EE7541D6306EFA0879DD3E70DF3D5201C5EF058326F020D1D7E7EBA7C9D218B7E68F9F1299BE6EE0063FE96FD343E2F1748EA699E68DF4A3E754F1EF63199617AB193381178E9A330CD4060AE899B4A28FE3AFF2DAA7A78C12EF202C3C9DEF3CACD63A493200E9462031599EB9259749D47F43842E1E11415B74D190FF1B8133530AC8EE859D8848A73478EFA438C120BE5FDDA50357EB3C492C9000<br>
<br>
+**GEN AC COMMAND:** 80AE800042000000001100000000000000068200800080000682221108003357A30B22000000000000000000001F0302020202000000000000000000000000000000000000000000<br>
!**Generate AC answer:** 77299F2701809F360200149F260859DFB5116A36145C9F10120110A00103A20000000000000000000000FF9000<br>
<br>
-**ARQC : 9F260859DFB5116A36145C**<br>
<br>
Card Disconnected<br>
