package scalaz

trait Applic[F[_]] {
  def applic[A, B](f: F[A => B]): F[A] => F[B]

  def **[G[_] : Applic]: Applic[({type λ[α] = (F[α], G[α])})#λ] =
    new Applic[({type λ[α] = (F[α], G[α])})#λ] {
      def applic[A, B](f: (F[A => B], G[A => B])) = {
        case (a, b) => (Applic.this.applic(f._1)(a), implicitly[Applic[G]].applic(f._2)(b))
      }
    }

  def deriving[G[_]](implicit n: ^**^[G, F]): Applic[G] =
    new Applic[G] {
      def applic[A, B](f: G[A => B]) =
        k => n.pack(Applic.this.applic(n unpack f)(n unpack k))
    }
}

object Applic extends Applics

trait Applics {

  implicit val OptionApplic: Applic[Option] = new Applic[Option] {
    def applic[A, B](f: Option[A => B]) =
      a => f flatMap (a map _)
  }

  implicit val ListApplic: Applic[List] = new Applic[List] {
    def applic[A, B](f: List[A => B]) =
      a => f flatMap (a map _)
  }

  implicit val StreamApplic: Applic[Stream] = new Applic[Stream] {
    def applic[A, B](f: Stream[A => B]) =
      a => f flatMap (a map _)
  }

  implicit def Function1Applic[T]: Applic[({type λ[α] = Function1[T, α]})#λ] = new Applic[({type λ[α] = Function1[T, α]})#λ] {
    def applic[A, B](f: T => A => B) =
      g => x => f(x)(g(x))
  }

  implicit def Tuple2Applic[R: Semigroup]: Applic[({type λ[α]=(R, α)})#λ] = new Applic[({type λ[α]=(R, α)})#λ] {
    def applic[A, B](f: (R, A => B)) = {
      case (r, a) => (implicitly[Semigroup[R]].append(f._1, r), f._2(a))
    }
  }

  implicit def Tuple3Applic[R: Semigroup, S: Semigroup]: Applic[({type λ[α]=(R, S, α)})#λ] = new Applic[({type λ[α]=(R, S, α)})#λ] {
    def applic[A, B](f: (R, S, A => B)) = {
      case (r, s, a) => (implicitly[Semigroup[R]].append(f._1, r), implicitly[Semigroup[S]].append(f._2, s), f._3(a))
    }
  }

  implicit def Tuple4Applic[R: Semigroup, S: Semigroup, T: Semigroup]: Applic[({type λ[α]=(R, S, T, α)})#λ] = new Applic[({type λ[α]=(R, S, T, α)})#λ] {
    def applic[A, B](f: (R, S, T, A => B)) = {
      case (r, s, t, a) => (implicitly[Semigroup[R]].append(f._1, r), implicitly[Semigroup[S]].append(f._2, s), implicitly[Semigroup[T]].append(f._3, t), f._4(a))
    }
  }

  implicit def Tuple5Applic[R: Semigroup, S: Semigroup, T: Semigroup, U: Semigroup]: Applic[({type λ[α]=(R, S, T, U, α)})#λ] = new Applic[({type λ[α]=(R, S, T, U, α)})#λ] {
    def applic[A, B](f: (R, S, T, U, A => B)) = {
      case (r, s, t, u, a) => (implicitly[Semigroup[R]].append(f._1, r), implicitly[Semigroup[S]].append(f._2, s), implicitly[Semigroup[T]].append(f._3, t), implicitly[Semigroup[U]].append(f._4, u), f._5(a))
    }
  }

  implicit def Tuple6Applic[R: Semigroup, S: Semigroup, T: Semigroup, U: Semigroup, V: Semigroup]: Applic[({type λ[α]=(R, S, T, U, V, α)})#λ] = new Applic[({type λ[α]=(R, S, T, U, V, α)})#λ] {
    def applic[A, B](f: (R, S, T, U, V, A => B)) = {
      case (r, s, t, u, v, a) => (implicitly[Semigroup[R]].append(f._1, r), implicitly[Semigroup[S]].append(f._2, s), implicitly[Semigroup[T]].append(f._3, t), implicitly[Semigroup[U]].append(f._4, u), implicitly[Semigroup[V]].append(f._5, v), f._6(a))
    }
  }

  implicit def Tuple7Applic[R: Semigroup, S: Semigroup, T: Semigroup, U: Semigroup, V: Semigroup, W: Semigroup]: Applic[({type λ[α]=(R, S, T, U, V, W, α)})#λ] = new Applic[({type λ[α]=(R, S, T, U, V, W, α)})#λ] {
    def applic[A, B](f: (R, S, T, U, V, W, A => B)) = {
      case (r, s, t, u, v, w, a) => (implicitly[Semigroup[R]].append(f._1, r), implicitly[Semigroup[S]].append(f._2, s), implicitly[Semigroup[T]].append(f._3, t), implicitly[Semigroup[U]].append(f._4, u), implicitly[Semigroup[V]].append(f._5, v), implicitly[Semigroup[W]].append(f._6, w), f._7(a))
    }
  }

}
