package euiti.mariobros.entities


import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import euiti.mariobros.MarioBros
import euiti.mariobros.screens.MainScreen

class Player(private val world: World, screen: MainScreen) : Sprite(screen.textureAtlas.findRegion("walk_right1")) {


    private val mario: TextureRegion

    var body: Body? = null
        private set

    private var centerMass: Vector2? = null
    private var impulseUp: Vector2? = null
    private var impulseRight: Vector2? = null
    private var impulseLeft: Vector2? = null

    init {


        defineMario()
        defineMov()


        mario = TextureRegion(texture, 2, 188, 36, 64)

        setBounds(0f, 0f, 12 / MarioBros.PPM, 24 / MarioBros.PPM)
        setRegion(mario)

    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(mario, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
    }

    private fun defineMov() {
        centerMass = this.body!!.worldCenter
        impulseUp = Vector2(0f, 4f)
        impulseRight = Vector2(0.1f, 0f)
        impulseLeft = Vector2(-0.1f, 0f)
    }

    private fun defineMario() {
        val bodyDef = BodyDef()

        bodyDef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM)
        bodyDef.type = BodyDef.BodyType.DynamicBody
        body = world.createBody(bodyDef)

        val fixtureDef = FixtureDef()
        val shape = CircleShape()
        shape.radius = 10 / MarioBros.PPM
        fixtureDef.shape = shape
        body!!.createFixture(fixtureDef).userData = this

    }

    fun update() {
        setPosition(body!!.position.x - width / 2, body!!.position.y - height / 2)
    }


    fun jump() {
        body!!.applyLinearImpulse(impulseUp!!, centerMass!!, true)
    }

    fun moveRight() {
        if (body!!.linearVelocity.x <= 2)
            body!!.applyLinearImpulse(impulseRight!!, centerMass!!, true)
    }

    fun moveLeft() {
        if (body!!.linearVelocity.x >= -2)
            body!!.applyLinearImpulse(impulseLeft!!, centerMass!!, true)

    }
}
