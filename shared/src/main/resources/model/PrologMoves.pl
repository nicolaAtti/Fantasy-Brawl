% specMove -- Defines a special move that can be performed by a character
% specMove(@Name,@DamageType,@MoveType,@BaseValue,@ModifiersList,@AlterationsList,@RemovedAlterations,@ManaCost,@NOfTargets)
%
% Author: Nicola Atti

spec_move('Lay On Hands','StandardHeal','Spell',50,[],[],[],15,1).
spec_move('Censure','BuffDebuff','Spell',0,[],['Silenced'],[],30,1).
spec_move('Holy Smite','StandardDamage','Melee',30,[],[],['Asleep'],25,1).
spec_move('Bolster Faith','BuffDebuff','Spell',0,['Bolstered Faith'],[],[],40,4).
spec_move('Poison Vial','StandardDamage','Ranged',20,[],['Poisoned'],['Asleep'],15,1).
spec_move('Flash Grenade','BuffDebuff','Ranged',0,[],['Blinded'],['Asleep'],20,4).
spec_move('Backstab','StandardDamage','Melee',60,[],[],['Asleep'],15,1).
spec_move('Preparation','BuffDebuff','Spell',0,['Prepared'],[],[],20,1).
spec_move('Second Wind','Percentage','Spell',110,[],[],[],20,1).
spec_move('Sismic Slam','StandardDamage','Melee',40,[],[],['Asleep'],15,4).
spec_move('Skullcrack','StandardDamage','Melee',30,[],['Stunned'],['Asleep'],20,1).
spec_move('Berserker Rage','BuffDebuff','Spell',0,[],['Berserk'],[],30,1).
spec_move('Freezing Wind','StandardDamage','Spell',25,[],['Frozen'],['Asleep'],25,4).
spec_move('Ice Lance','StandardDamage','Spell',40,[],[],['Asleep'],20,1).
spec_move('Concentration','BuffDebuff','Spell',0,['Concentrated'],[],[],15,1).
spec_move('Counterspell','BuffDebuff','Spell',0,[],['Silenced'],['Asleep'],15,1).
spec_move('Shield Bash','StandardDamage','Melee',10,[],['Silenced'],['Asleep'],20,1).
spec_move('Adrenaline Rush','BuffDebuff','Spell',0,['Sped Up'],[],[],15,1).
spec_move('Iron Will','BuffDebuff','Spell',0,['Will Of Iron'],[],[],15,1).
spec_move('Mighty Slam','StandardDamage','Melee',40,[],[],['Asleep'],25,1).
spec_move('Trip Attack','StandardDamage','Melee',15,['Slowed'],[],['Asleep'],15,1).
spec_move('Sunder Defenses','StandardDamage','Melee',5,['Sundered Defences'],[],['Asleep'],15,1).
spec_move('Chrono Shift','Percentage','Spell',15,[],[],['Asleep'],30,4).
spec_move('Haste','BuffDebuff','Spell',0,['Sped Up'],[],[],20,1).
spec_move('Hypnosis','BuffDebuff','Spell',0,[],['Asleep'],[],30,1).
spec_move('Arcane Blast','StandardDamage','Spell',50,[],[],['Asleep'],25,1).
spec_move('Pyroblast','StandardDamage','Spell',35,[],[],['Asleep'],15,1).
spec_move('Dragon Breath','StandardDamage','Spell',15,[],['Stunned'],['Asleep'],20,1).
spec_move('Flamestrike','StandardDamage','Spell',40,[],[],['Asleep'],25,4).
spec_move('Holy Nova','StandardDamage','Spell',30,[],[],['Asleep'],20,4).
spec_move('Radiance','Percentage','Spell',130,[],[],[],40,4).
spec_move('Chastise','BuffDebuff','Spell',0,[],['Stunned'],['Asleep'],20,1).

spec_move('Aimed Shot','StandardDamage','Ranged',35,[],[],['Asleep'],15,1).
spec_move('Arrow Volley','StandardDamage','Ranged',20,[],[],['Asleep'],25,4).
spec_move('Trueshot','BuffDebuff','Spell',0,['Trueshot'],[],[],20,1).
spec_move('Bear Trap','BuffDebuff','Spell',10,[],['Frozen'],['Asleep'],15,1).

spec_move('Nature Wrath','StandardDamage','Spell',30,[],[],['Asleep'],15,1).
spec_move('Benefic Spores','BuffDebuff','Spell',0,[],['Regeneration'],[],25,4).
spec_move('Antidote','BuffDebuff','Spell',0,[],[],['Poisoned'],20,1).
spec_move('Entangling Roots','BuffDebuff','Spell',0,[],['Frozen'],[],15,1).
spec_move('Quick Stab','StandardDamage','Melee',30,[],[],['Asleep'],15,1).
spec_move('Fan Of Knives','StandardDamage','Ranged',25,[],[],['Asleep'],10,4).
spec_move('Duelist Step','BuffDebuff','Spell',0,['Quick Reflexes','Sped Up'],[],[],20,1).
spec_move('Pummel','StandardDamage','Melee',10,[],['Silenced'],['Asleep'],15,1).
%modifier --- Defines the duration,power and the interested attribute for every existing modifier
%modifier(+ModId,-SubStatistic,-TurnDuration,-Value)
%
% Author: Nicola Atti

modifier('Concentrated','MagicalDamage',1,30).
modifier('Prepared','CriticalChance',1,25).
modifier('Bolstered Faith','MagicalDefence',1,20).
modifier('Sundered Defences','PhysicalDefence',2,-20).
modifier('Slowed','Speed',1,-1).
modifier('Will Of Iron','MagicalDefence',2,10).
modifier('Sped Up','Speed',2,2).
modifier('Quick Reflexes','PhysicalDefence',2,10).
modifier('Trueshot','PhysicalCriticalDamage',5,25).