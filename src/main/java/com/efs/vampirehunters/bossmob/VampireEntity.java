package com.efs.vampirehunters.bossmob;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.efs.vampirehunters.VampireHunters;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class VampireEntity extends HostileEntity {
	   private static final TrackedData<Integer> TRACKED_ENTITY_ID_1;
	   private static final TrackedData<Integer> TRACKED_ENTITY_ID_2;
	   private static final TrackedData<Integer> TRACKED_ENTITY_ID_3;
	   private static final List<TrackedData<Integer>> TRACKED_ENTITY_IDS;
	   private static final TrackedData<Integer> INVUL_TIMER;
	   public static final float field_30519 = 0.05F;
   public static final int field_30515 = 50;
   public static final int field_30516 = 40;
   private final ServerBossBar bossBar;
   public static final int field_30517 = 7;

   public VampireEntity(EntityType<? extends VampireEntity> entityType, World world) {
	        super(entityType, world);
	        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);
	        this.setHealth(this.getMaxHealth());
	        this.getNavigation().setCanSwim(true);
	        this.experiencePoints = 50;
   }

   protected void initGoals() {
      this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
      this.goalSelector.add(8, new LookAroundGoal(this));
      this.initCustomGoals();
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
protected void initCustomGoals() {
      this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
      this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D));
      this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
      this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(BatEntity.class));
      this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));
      this.targetSelector.add(3, new FollowTargetGoal(this, MerchantEntity.class, false));
      this.targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, true));
   }

   public static DefaultAttributeContainer.Builder createVampireAttributes() {
      return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D);
   }

   protected void initDataTracker() {
	      super.initDataTracker();
	      this.dataTracker.startTracking(TRACKED_ENTITY_ID_1, 0);
	      this.dataTracker.startTracking(TRACKED_ENTITY_ID_2, 0);
	      this.dataTracker.startTracking(TRACKED_ENTITY_ID_3, 0);
	      this.dataTracker.startTracking(INVUL_TIMER, 0);
	   }
   public void writeCustomDataToNbt(NbtCompound nbt) {
	      super.writeCustomDataToNbt(nbt);
	      nbt.putInt("Invul", this.getInvulnerableTimer());
	   }

	   public void readCustomDataFromNbt(NbtCompound nbt) {
	      super.readCustomDataFromNbt(nbt);
	      this.setInvulTimer(nbt.getInt("Invul"));
	      if (this.hasCustomName()) {
	         this.bossBar.setName(this.getDisplayName());
	      }

	   }
   public void tick() {
      super.tick();
   }

   public void tickMovement() {
      if (this.isAlive()) {
         boolean bl = this.burnsInDaylight() && this.isAffectedByDaylight();
            if (bl) {
               this.setOnFireFor(8);
            }
         }
      

      super.tickMovement();
   }

   protected boolean burnsInDaylight() {
      return true;
   }


   public boolean tryAttack(Entity target) {
      boolean bl = super.tryAttack(target);
      if (bl) {
         float f = this.world.getLocalDifficulty(this.getBlockPos()).getLocalDifficulty();
         if (this.getMainHandStack().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
            target.setOnFireFor(2 * (int)f);
         }
      }

      return bl;
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.ENTITY_ZOMBIE_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_ZOMBIE_DEATH;
   }

   protected SoundEvent getStepSound() {
      return SoundEvents.ENTITY_ZOMBIE_STEP;
   }

   protected void playStepSound(BlockPos pos, BlockState state) {
      this.playSound(this.getStepSound(), 0.15F, 1.0F);
   }

   public EntityGroup getGroup() {
      return EntityGroup.UNDEAD;
   }

   protected void initEquipment(LocalDifficulty difficulty) {
      super.initEquipment(difficulty);
      if (this.random.nextFloat() < (this.world.getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
         int i = this.random.nextInt(3);
         if (i == 0) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
         } else {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
         }
      }

   }
   public boolean damage(DamageSource source, float amount) {
	      if (this.isInvulnerableTo(source)) {
	         return false;
	      } else if (source != DamageSource.DROWN && !(source.getAttacker() instanceof VampireEntity)) {
	         if (this.getInvulnerableTimer() > 0 && source != DamageSource.OUT_OF_WORLD) {
	        	 source.getSource().damage(DamageSource.MAGIC, amount);
	            return false;
	         } else {
	            Entity entity2;
	            if (this.shouldRenderOverlay()) {
	               entity2 = source.getSource();
	               if (entity2 instanceof PersistentProjectileEntity) {
	                  return false;
	               }
	            }

	               return super.damage(source, amount);
	         }
	      } else {
	         return false;
	      }
	   }
   
   public int getInvulnerableTimer() {
	      return (Integer)this.dataTracker.get(INVUL_TIMER);
	   }

	   public void setInvulTimer(int ticks) {
	      this.dataTracker.set(INVUL_TIMER, ticks);
	   }

	public int getTrackedEntityId(int headIndex) {
	      return (Integer)this.dataTracker.get((TrackedData<?>)TRACKED_ENTITY_IDS.get(headIndex));
	   }

	public void setTrackedEntityId(int headIndex, int id) {
	      this.dataTracker.set((TrackedData<Integer>)TRACKED_ENTITY_IDS.get(headIndex), id);
	   }
	   
	   public boolean shouldRenderOverlay() {
		      return this.getHealth() <= this.getMaxHealth() / 2.0F;
		   }

		   protected boolean canStartRiding(Entity entity) {
		      return false;
		   }

		   public boolean canUsePortals() {
		      return false;
		   }

   public void onKilledOther(ServerWorld world, LivingEntity other) {
      super.onKilledOther(world, other);
      if ((world.getDifficulty() == Difficulty.NORMAL || world.getDifficulty() == Difficulty.HARD) && other instanceof VillagerEntity) {
         if (world.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
            return;
         }

         VillagerEntity villagerEntity = (VillagerEntity)other;
         VampireEntity vampireVillagerEntity = (VampireEntity)villagerEntity.convertTo(VampireHunters.VAMPIRE, false);
         vampireVillagerEntity.initialize(world, world.getLocalDifficulty(vampireVillagerEntity.getBlockPos()), SpawnReason.CONVERSION, null, (NbtCompound)null);

         if (!this.isSilent()) {
            world.syncWorldEvent((PlayerEntity)null, WorldEvents.ZOMBIE_INFECTS_VILLAGER, this.getBlockPos(), 0);
         }
      }

   }
   
   protected void mobTick() {
	         super.mobTick();

	         if (this.getTarget() != null) {
	            this.setTrackedEntityId(0, this.getTarget().getId());
	         } else {
	            this.setTrackedEntityId(0, 0);
	         }

	         if (this.age % 20 == 0) {
	            this.heal(1.0F);
	         }

	         this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
	      
	   }
   
   public void onSummoned() {
	      this.setInvulTimer(220);
	      this.bossBar.setPercent(0.0F);
	      this.setHealth(this.getMaxHealth() / 3.0F);
	   }
   
   public void onStartedTrackingBy(ServerPlayerEntity player) {
	      super.onStartedTrackingBy(player);
	      this.bossBar.addPlayer(player);
	   }

	   public void onStoppedTrackingBy(ServerPlayerEntity player) {
	      super.onStoppedTrackingBy(player);
	      this.bossBar.removePlayer(player);
	   }

	   public void setCustomName(@Nullable Text name) {
	      super.setCustomName(name);
	      this.bossBar.setName(this.getDisplayName());
	   }

   protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
      return this.isBaby() ? 0.93F : 1.74F;
   }

   public boolean canPickupItem(ItemStack stack) {
      return stack.isOf(Items.EGG) && this.isBaby() && this.hasVehicle() ? false : super.canPickupItem(stack);
   }

   public boolean canGather(ItemStack stack) {
      return stack.isOf(Items.GLOW_INK_SAC) ? false : super.canGather(stack);
   }

   /**
    * Returns the item stack this entity will drop when killed by a charged creeper.
    */

   static {
	      TRACKED_ENTITY_ID_1 = DataTracker.registerData(WitherEntity.class, TrackedDataHandlerRegistry.INTEGER);
	      TRACKED_ENTITY_ID_2 = DataTracker.registerData(WitherEntity.class, TrackedDataHandlerRegistry.INTEGER);
	      TRACKED_ENTITY_ID_3 = DataTracker.registerData(WitherEntity.class, TrackedDataHandlerRegistry.INTEGER);
	      TRACKED_ENTITY_IDS = ImmutableList.of(TRACKED_ENTITY_ID_1, TRACKED_ENTITY_ID_2, TRACKED_ENTITY_ID_3);
	      INVUL_TIMER = DataTracker.registerData(WitherEntity.class, TrackedDataHandlerRegistry.INTEGER);
      DataTracker.registerData(VampireEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
      DataTracker.registerData(VampireEntity.class, TrackedDataHandlerRegistry.INTEGER);
      DataTracker.registerData(VampireEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
   }

}
