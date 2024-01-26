package autoThings

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d

enum class AprilLocations(val pose: Pose2d) {
    ONE(Pose2d(TODO("get the value"))),
    TWO(Pose2d(TODO("get the value"))),
    THREE(Pose2d(TODO("get the value"))),
    FOUR(Pose2d(TODO("get the value"))),
    FIVE(Pose2d(TODO("get the value"))),
    SIX(Pose2d(TODO("get the value")))
}

fun getAprilLoc(tag: Int): Pose2d {
    return when (tag) {
        1 -> AprilLocations.ONE.pose
        2 -> AprilLocations.TWO.pose
        3 -> AprilLocations.THREE.pose
        4 -> AprilLocations.FOUR.pose
        5 -> AprilLocations.FIVE.pose
        6 -> AprilLocations.SIX.pose
        else -> throw Exception("not a tag on the field")
    }
}

fun getAprilVec(tag: Int): Vector2d {
    return when (tag) {
        1 -> Vector2d(AprilLocations.ONE.pose.x, AprilLocations.ONE.pose.y)
        2 -> Vector2d(AprilLocations.TWO.pose.x, AprilLocations.TWO.pose.y)
        3 -> Vector2d(AprilLocations.THREE.pose.x, AprilLocations.THREE.pose.y)
        4 -> Vector2d(AprilLocations.FOUR.pose.x, AprilLocations.FOUR.pose.y)
        5 -> Vector2d(AprilLocations.FIVE.pose.x, AprilLocations.FIVE.pose.y)
        6 -> Vector2d(AprilLocations.SIX.pose.x, AprilLocations.SIX.pose.y)
        else -> throw Exception("not a tag on the field")
    }
}